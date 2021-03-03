    propertyapp.directive("bootstrapbreadcrumb", function() { 
		return {
			templateUrl: '../html/components/bootstrap/breadcrumb.html',
			restrict: 'E',
			transclude: false,
			scope: {
				breadcrumbItems: "=items",
				breadcrumbId: "@id"
			}
		};	
	});
