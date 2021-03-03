	propertyapp.directive("bootstrapcarousel", function() { 
		return {
			templateUrl: '../html/components/bootstrap/carousel.html',
			restrict: 'E',
			transclude: false,
			scope: {
				carouselItems: "=items",
				carouselId: "@id"
			},
			link: function(scope, element, attr) {
				//angular intercept click events, preventing working correctly
			}
		};	
	});