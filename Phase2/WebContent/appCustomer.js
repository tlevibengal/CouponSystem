var app = angular.module('myApp', []);
app.controller('myController', function($scope, $http) {

	
	var userNamefromSession = "";
	$http.get("rest/customers/getSessionUser").then(function(response) {
		$scope.userNamefromSession = response.headers()['user'];
	}, function(response) {
		$scope.userNamefromSession = "error getting user";
	});
	
	
	
	$scope.refreshTables = function() {
		$scope.getAllExistingCoupons();

	}

//	$scope.resetFields = function() {
//		$scope.coupon.id = "";
//
//	}
		
	
	
	
	$scope.couponView = "couponIcons.html"
		$scope.purchasedcouponView = "purchasedcouponIcons.html"

	
	
	
		$scope.buyCoupon = function(id) {
	 $http.get("rest/customers/buy/"+id).then(
				function(response) {
					$scope.gotCoupon = "The coupon you requested has been added to your account";
				}, function(response) {
					$scope.gotCoupon = "The coupon could not be purchaised it has expired or is not in inventory.";

				});
	}


	// WORKS DO NOT TOUCH//
	$scope.getAllExistingCoupons = function() {
		$scope.sortColumn = "couponId"
			$scope.sortView = "icon"
		$scope.coupons = $http.get("rest/customers/getAllCoupons").then(
				function(response) {
					$scope.coupons = response.data;

					$scope.resetFields();
					$scope.sortColumn = "couponName";
					
				}, function(response) {
					$scope.coupons = "Empty coupon list";

				});
	}

	
		$scope.	getAllCoupons = function() {
			$scope.sortColumn = "couponId"
				$scope.sortView = "icon"

		$scope.purchasedcoupons = $http.get("rest/customers/getAllpurchasedCoupons").then(
				function(response) {
					$scope.purchasedcoupons = response.data;
					$scope.resetFields();
				}, function(response) {
					$scope.purchasedcoupons = "Empty coupon list";

				});
	}

	$scope.goodBye = function() {
		$http.get("rest/customers/quit").then(function(response) {
			$scope.goodResponse = response.headers()['quit'];

		});
	}


});
