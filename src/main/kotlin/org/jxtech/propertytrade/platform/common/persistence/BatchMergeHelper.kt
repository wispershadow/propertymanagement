package org.jxtech.propertytrade.platform.common.persistence

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.util.ReflectionUtils

object BatchMergeHelper {
    private val logger: Logger = LoggerFactory.getLogger(BatchMergeHelper::class.java)

    fun <SAVEREQ, SEARCHKEY, ENTITY, ENTITYKEY> performBatchMerge(saveRequests: List<SAVEREQ>, savedByUser: String,
        preMergeHook: (BatchMergeContext<SAVEREQ, SEARCHKEY, ENTITY, ENTITYKEY>, String) -> Unit = {
                _, _ ->
        },
        searchKeyAttribute: String,
        entityIdAttribute: String,
        searchFun: (List<SEARCHKEY>, BatchMergeContext<SAVEREQ, SEARCHKEY, ENTITY, ENTITYKEY>) -> List<ENTITY>,
        entityTransformer: (SEARCHKEY, SAVEREQ, BatchMergeContext<SAVEREQ, SEARCHKEY, ENTITY, ENTITYKEY>, String) -> ENTITY,
        entitySaveFun: (Iterable<ENTITY>, BatchMergeContext<SAVEREQ, SEARCHKEY, ENTITY, ENTITYKEY>) -> Iterable<ENTITY>): List<ENTITYKEY> {
        return performBatchMerge(saveRequests, savedByUser, preMergeHook, searchFun, { saveReq, _ ->
            getAttributeValueByName(saveReq as Any, searchKeyAttribute) as SEARCHKEY
        }, entityTransformer, { entity, _ ->
            getAttributeValueByName(entity as Any, entityIdAttribute) as ENTITYKEY
        }, { entity, _ ->
            getAttributeValueByName(entity as Any, searchKeyAttribute) as SEARCHKEY
        }, entitySaveFun)
    }

    private fun getAttributeValueByName(entity: Any, attributeName: String): Any {
        val field = ReflectionUtils.findField(entity::class.java, attributeName)
        return ReflectionUtils.getField(field, entity)
    }

    fun <SAVEREQ, SEARCHKEY, ENTITY, ENTITYKEY> performBatchMerge(saveRequests: List<SAVEREQ>, savedByUser: String,
        preMergeHook: (BatchMergeContext<SAVEREQ, SEARCHKEY, ENTITY, ENTITYKEY>, String) -> Unit = {
            _, _ ->
        },
        searchFun: (List<SEARCHKEY>, BatchMergeContext<SAVEREQ, SEARCHKEY, ENTITY, ENTITYKEY>) -> List<ENTITY>,
        searchKeyExtractor: (SAVEREQ, BatchMergeContext<SAVEREQ, SEARCHKEY, ENTITY, ENTITYKEY>) -> SEARCHKEY,
        entityTransformer: (SEARCHKEY, SAVEREQ, BatchMergeContext<SAVEREQ, SEARCHKEY, ENTITY, ENTITYKEY>, String) -> ENTITY,
        entityKeyExtractor: (ENTITY, BatchMergeContext<SAVEREQ, SEARCHKEY, ENTITY, ENTITYKEY>) -> ENTITYKEY,
        entitySearchKeyExtractor: (ENTITY, BatchMergeContext<SAVEREQ, SEARCHKEY, ENTITY, ENTITYKEY>) -> SEARCHKEY,
        entitySaveFun: (Iterable<ENTITY>, BatchMergeContext<SAVEREQ, SEARCHKEY, ENTITY, ENTITYKEY>) -> Iterable<ENTITY>): List<ENTITYKEY> {
        val batchMergeContext = BatchMergeContext<SAVEREQ, SEARCHKEY, ENTITY, ENTITYKEY>()
        batchMergeContext.addSaveRequests(saveRequests, searchKeyExtractor)
        preMergeHook.invoke(batchMergeContext, savedByUser)
        val allDistinctSearchKeys = batchMergeContext.getDistinctSearchKeys()
        val existingSearchKeys = mutableSetOf<SEARCHKEY>()
        val entityList = searchFun.invoke(allDistinctSearchKeys.toList(), batchMergeContext)
        batchMergeContext.addEntitiesToCache(entityList.map { entity ->
            val existingSearchKey = entitySearchKeyExtractor.invoke(entity, batchMergeContext)
            existingSearchKeys.add(existingSearchKey)
            existingSearchKey to entity
        })
        logger.debug("Search keys already exist: {}", existingSearchKeys)
        val newEntitiesToSave = allDistinctSearchKeys.minus(existingSearchKeys).map {searchkey ->
            val saveRequest = batchMergeContext.getSaveRequestBySearchKey(searchkey)
            entityTransformer.invoke(searchkey, saveRequest, batchMergeContext, savedByUser)
        }
        val savedEntities = entitySaveFun.invoke(newEntitiesToSave, batchMergeContext)
        batchMergeContext.addEntitiesToCache(savedEntities.map {entity ->
            val existingSearchKey = entitySearchKeyExtractor.invoke(entity, batchMergeContext)
            existingSearchKeys.add(existingSearchKey)
            existingSearchKey to entity
        })
        return batchMergeContext.getEntityKeysBySearchKeys(entityKeyExtractor).also {
            batchMergeContext.clear()
        }
    }
}

class BatchMergeContext<SAVEREQ, SEARCHKEY, ENTITY, ENTITYKEY> {
    private val searchKeysInSequence = mutableListOf<SEARCHKEY>()
    private val distinctSearchKeys = mutableSetOf<SEARCHKEY>()
    private val saveRequestsBySearchKey = mutableMapOf<SEARCHKEY, SAVEREQ>()
    private val tempEntityCache = mutableMapOf<SEARCHKEY, ENTITY>()
    private val contextAttributes: MutableMap<String, Any> = mutableMapOf()


    fun addSaveRequests(saveRequests: List<SAVEREQ>, searchKeyExtractor: (SAVEREQ, BatchMergeContext<SAVEREQ, SEARCHKEY, ENTITY, ENTITYKEY>) -> SEARCHKEY) {
        saveRequests.forEach {saveReq ->
            val searchKey = searchKeyExtractor.invoke(saveReq, this)
            searchKeysInSequence.add(searchKey)
            distinctSearchKeys.add(searchKey)
            saveRequestsBySearchKey[searchKey] = saveReq
        }
    }

    fun addEntitiesToCache(entities: Iterable<Pair<SEARCHKEY, ENTITY>>) {
        tempEntityCache.putAll(entities)
    }

    fun addContextAttribute(attribKey: String, attribValue: Any) {
        contextAttributes[attribKey] = attribValue
    }

    fun getContextAttribute(attribKey: String): Any? {
        return contextAttributes[attribKey]
    }

    fun getSearchKeysInSequence(): List<SEARCHKEY> {
        return searchKeysInSequence.toList()
    }

    fun getDistinctSearchKeys(): Set<SEARCHKEY> {
        return distinctSearchKeys.toSet()
    }

    fun getSaveRequestBySearchKey(searchkey: SEARCHKEY): SAVEREQ {
        return saveRequestsBySearchKey.getValue(searchkey)
    }

    fun getSaveRequestsBySearchKey(): Map<SEARCHKEY, SAVEREQ> {
        return saveRequestsBySearchKey.toMap()
    }

    fun getEntityKeysBySearchKeys(
        entityKeyExtractor: (ENTITY, BatchMergeContext<SAVEREQ, SEARCHKEY, ENTITY, ENTITYKEY>) -> ENTITYKEY): List<ENTITYKEY> {
        return searchKeysInSequence.map {
            entityKeyExtractor.invoke(tempEntityCache.getValue(it), this)
        }
    }

    fun clear() {
        searchKeysInSequence.clear()
        distinctSearchKeys.clear()
        saveRequestsBySearchKey.clear()
        tempEntityCache.clear()
        contextAttributes.clear()
    }

}
