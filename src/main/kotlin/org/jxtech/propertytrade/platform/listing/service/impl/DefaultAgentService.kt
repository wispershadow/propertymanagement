package org.jxtech.propertytrade.platform.listing.service.impl

import org.jxtech.propertytrade.platform.common.persistence.BatchMergeContext
import org.jxtech.propertytrade.platform.common.persistence.BatchMergeHelper
import org.jxtech.propertytrade.platform.common.persistence.setCommonFieldsBeforeCreate
import org.jxtech.propertytrade.platform.common.service.AddressService
import org.jxtech.propertytrade.platform.common.service.data.AddressSaveRequest
import org.jxtech.propertytrade.platform.listing.persistence.entity.Agent
import org.jxtech.propertytrade.platform.listing.persistence.entity.AgentType
import org.jxtech.propertytrade.platform.listing.persistence.entity.CompanyAgent
import org.jxtech.propertytrade.platform.listing.persistence.entity.PersonalAgent
import org.jxtech.propertytrade.platform.listing.persistence.repository.AgentRepository
import org.jxtech.propertytrade.platform.listing.service.AgentService
import org.jxtech.propertytrade.platform.listing.service.data.AgentSaveRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DefaultAgentService(private val agentRepository: AgentRepository,
    private val addressService: AddressService
): AgentService {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(DefaultAgentService::class.java)
        private val CONTEXT_ATTRIBUTE_ADDRESSMAP = "addressMap"
        private val CONTEXT_ATTRIBUTE_COMPANYMAP = "companyMap"
    }


    override fun batchSave(saveRequests: List<AgentSaveRequest>, savedByUser: String): List<Long> {
        return BatchMergeHelper.performBatchMerge<AgentSaveRequest, String, Agent, Long>(
            saveRequests = saveRequests, savedByUser = savedByUser,
            preMergeHook = this::batchSaveAgentCompany,
            searchFun = this::searchPersonalAgent,
            searchKeyExtractor = this::extractPersonalAgentSearchKey,
            entityTransformer = this::convertAgentSaveReqToPersonalAgent,
            entityKeyExtractor = this::extractAgentIdFromAgent,
            entitySearchKeyExtractor = this::extractPersonalNameFromAgent,
            entitySaveFun = this::saveAgent
        )
    }


    fun batchSaveAgentCompany(batchMergeContext: BatchMergeContext<AgentSaveRequest, String, Agent, Long>, savedByUser: String) {
        val saveRequests = batchMergeContext.getSaveRequestsBySearchKey().values.toList()
        val comapnyAgentIds =  BatchMergeHelper.performBatchMerge<AgentSaveRequest, String, Agent, Long>(
            saveRequests = saveRequests, savedByUser = savedByUser,
            preMergeHook = this::saveAddresses,
            searchFun = this::searchCompanyAgents,
            searchKeyExtractor = this::extractCompanyAgentSearchKey,
            entityTransformer = this::convertAgentSaveReqToCompanyAgent,
            entityKeyExtractor = this::extractAgentIdFromAgent,
            entitySearchKeyExtractor = this::extractCompanyNameFromAgent,
            entitySaveFun = this::saveAgent
        )
        val companyAgentMapWithId = saveRequests.mapIndexed {index, agentSaveRequest ->
            agentSaveRequest to comapnyAgentIds[index]
        }.toMap()
        batchMergeContext.addContextAttribute(CONTEXT_ATTRIBUTE_COMPANYMAP, companyAgentMapWithId)
    }

    private fun saveAddresses(batchMergeContext: BatchMergeContext<AgentSaveRequest, String, Agent, Long>, savedByUser: String) {
        val savedRequestsBySearchKey = batchMergeContext.getSaveRequestsBySearchKey()
        val addressSaveRequests = savedRequestsBySearchKey.map {(key, saveReq) ->
            saveReq.addressSaveRequest
        }
        val addressIds = addressService.batchSave(addressSaveRequests, savedByUser)
        val addressMapWithId = addressIds.mapIndexed { index, addressId ->
            addressSaveRequests[index] to addressId
        }.toMap()
        batchMergeContext.addContextAttribute(CONTEXT_ATTRIBUTE_ADDRESSMAP, addressMapWithId)
    }

    private fun searchCompanyAgents(companyNamesToSearch: List<String>,
        batchMergeContext: BatchMergeContext<AgentSaveRequest, String, Agent, Long>): List<Agent> {
        val resultAgentList = mutableListOf<Agent>()
        companyNamesToSearch.forEach {companyName ->
            val companyAgentOptional = agentRepository.findCompanyAgentByName(companyName)
            if (companyAgentOptional.isPresent) {
                resultAgentList.add(companyAgentOptional.get())
            }
        }
        return resultAgentList
    }

    private fun searchPersonalAgent(personalAgentNamesToSearch: List<String>,
        batchMergeContext: BatchMergeContext<AgentSaveRequest, String, Agent, Long>): List<Agent> {
        val resultAgentList = mutableListOf<Agent>()
        personalAgentNamesToSearch.forEach {personalAgentName ->
            val agentNameParts = personalAgentName.split("|")
            val firstName = agentNameParts[0]
            val lastName = agentNameParts[1]
            val midName: String? = if (agentNameParts.size > 2) {
                agentNameParts[2]
            }
            else {
                null
            }
            val personalAgentOptional = agentRepository.findPersonalAgentByName(firstName, lastName, midName)
            if (personalAgentOptional.isPresent) {
                resultAgentList.add(personalAgentOptional.get())
            }
        }
        return resultAgentList
    }

    private fun extractCompanyAgentSearchKey(agentSaveRequest: AgentSaveRequest,
        batchMergeContext: BatchMergeContext<AgentSaveRequest, String, Agent, Long>): String {
        return agentSaveRequest.agentCompanyName
    }

    private fun extractPersonalAgentSearchKey(agentSaveRequest: AgentSaveRequest,
        batchMergeContext: BatchMergeContext<AgentSaveRequest, String, Agent, Long>): String {
        val resultBuilder = StringBuilder()
        resultBuilder.append(agentSaveRequest.firstName).append("|")
        resultBuilder.append(agentSaveRequest.lastName)
        agentSaveRequest.middleName?.let {
            resultBuilder.append("|").append(it)
        }
        return resultBuilder.toString()
    }

    private fun extractCompanyNameFromAgent(agent: Agent,
        batchMergeContext: BatchMergeContext<AgentSaveRequest, String, Agent, Long>): String {
        return (agent as CompanyAgent).name
    }

    private fun extractPersonalNameFromAgent(agent: Agent,
        batchMergeContext: BatchMergeContext<AgentSaveRequest, String, Agent, Long>): String {
        val personalAgent = agent as PersonalAgent
        val resultBuilder = StringBuilder()
        resultBuilder.append(personalAgent.firstName).append("|")
        resultBuilder.append(personalAgent.lastName)
        personalAgent.middleName?.let {
            resultBuilder.append("|").append(it)
        }
        return resultBuilder.toString()
    }

    private fun extractAgentIdFromAgent(agent: Agent,
        batchMergeContext: BatchMergeContext<AgentSaveRequest, String, Agent, Long>): Long {
        return agent.id
    }

    private fun convertAgentSaveReqToCompanyAgent(companyAgentName: String, agentSaveRequest: AgentSaveRequest,
        batchMergeContext: BatchMergeContext<AgentSaveRequest, String, Agent, Long>, savedByUser: String): Agent {
        val addressMapWithId = batchMergeContext.getContextAttribute(CONTEXT_ATTRIBUTE_ADDRESSMAP) as Map<AddressSaveRequest, Long>
        return CompanyAgent().apply {
            this.name = agentSaveRequest.agentCompanyName
            this.agentType = AgentType.FIRM
            this.addressId = addressMapWithId.getValue(agentSaveRequest.addressSaveRequest)
            this.setCommonFieldsBeforeCreate(savedByUser)
        }
    }


    private fun convertAgentSaveReqToPersonalAgent(personalAgentName: String, agentSaveRequest: AgentSaveRequest,
        batchMergeContext: BatchMergeContext<AgentSaveRequest, String, Agent, Long>, savedByUser: String): Agent {
        val companyMapWithId = batchMergeContext.getContextAttribute(CONTEXT_ATTRIBUTE_COMPANYMAP) as Map<AgentSaveRequest, Long>
        return PersonalAgent().apply {
            this.firstName = agentSaveRequest.firstName
            this.lastName = agentSaveRequest.lastName
            this.middleName = agentSaveRequest.middleName
            this.phone = agentSaveRequest.phone
            this.agentCompanyId = companyMapWithId.getValue(agentSaveRequest)
            this.setCommonFieldsBeforeCreate(savedByUser)
        }
    }

    private fun saveAgent(agentList: Iterable<Agent>,
        batchMergeContext: BatchMergeContext<AgentSaveRequest, String, Agent, Long>): Iterable<Agent> {
        return agentRepository.saveAll(agentList)
    }

}
