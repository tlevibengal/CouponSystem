var app = angular.module('myApp', []);
app.controller('myController', function($scope, $http) {

	$scope.refreshTables = function(id) {
		$scope.getAllCoups();

	}

	$scope.resetFields = function(id) {
		$scope.coupId = null;
		$scope.dlId = null;
		$scope.couponName = null;
		$scope.description = null;
		$scope.startDate = null;
		$scope.endDate = null;
		$scope.price = null;
		$scope.amount = null;
		$scope.type = null;
		$scope.image = null;
		$scope.upCoupId = null;
		$scope.upPrice = null;
		$scope.upDate = null;
		$scope.NoCoupon ="";

	}

	$scope.findCoupon = function(id) {
		var gotCoupon = [ {
			couponId : "",
			couponName : "",
			description : "",
			startDate : "",
			endDate : "",
			price : "",
			amount : "",
			couponType : "",
			image : ""
		} ];
		$scope.gotCoupon = gotCoupon;
		$http.get("rest/companies/findCoupon/" + id).then(function(response) {
			$scope.gotCoupon = response.data;
			$scope.resetFields();
		}, function(response) {
			$scope.NoCoupon = "could not be found in the system"


		});
	}
	$scope.couponView = "CompanyCouponIcons.html"
		$scope.fileLocation = ""

	// WORKS DO NOT TOUCH//
	$scope.getAllCoups = function() {
		$scope.coupons = $http.get("rest/companies/getAllCoupons").then(
				function(response) {
					$scope.coupons = response.data;
					$scope.resetFields();
					$scope.sortColumn = "couponName";
				}, function(response) {
					$scope.coupons = "Empty coupon list";

				});
	}
	
	$scope.getFileLocation = function() {
		$scope.gottonfileLocation = $http.get("rest/companies/getFileLocation").then(
				function(response) {
					$scope.gottonfileLocation = response.data;
				}, function(response) {
					$scope.gottonfileLocation = "Empty location";

				});
	}

	// DELETE CUSTOMER
	$scope.deleteCoup = function(id) {
		$http.get("rest/companies/delCoup/" + id).then(function(response) {
			$scope.deletedCoup = "The coupon (id:"+id+") you requested to remove havs been deleted succssefully."
			$scope.refreshTables();
			$scope.resetFields();
		}, function(response) {
			$scope.deletedCoup = "Coupon with id:"+id+" , could not be found and was not removed."


		});
	}
	//
	$scope.doUpdate = function(id, price, endDate) {
		$http.get(
				"rest/companies/updateCoupon/" + id + "/" + price + "/"
						+ endDate).then(function(response) {
			$scope.updatedCoup =  "The coupons new price and exparation date have been updated. (price:"+price+", end date:"+endDate+")."
			$scope.refreshTables();
		}, function(response) {
			$scope.updatedCoup =  "The coupons new price and exparation date could not be updated, please try again."
		});
	}

	$scope.create = function() {
		$http.get("rest/companies/createCoup/" + $scope.couponName + "/"
								+ $scope.description + "/" + $scope.startDate
								+ "/" + $scope.endDate + "/" + $scope.price
								+ "/" + $scope.amount + "/" + $scope.type).then(function(response) {
					$scope.createdCoupon = "The coupon you requested has been created successfully."
					$scope.resetFields();
				}, function(response) {
					$scope.createdCoupon = "An error occurred,The coupon was not created, please try again ."

				});
	}


	$scope.quit = function() {
		$http.get("rest/companies/quit").then(function(response) {
			$scope.goodResponse = response.headers()['quit'];

		});
	}

	var userNamefromSession = "";
	$http.get("rest/companies/getSessionUser").then(function(response) {
		$scope.userNamefromSession = response.headers()['user'];
	}, function(response) {
		$scope.userNamefromSession = "error getting user";
	});
	

	$scope.types = $http.get("rest/companies/loadTypes").then(
			function(response) {
				$scope.coupTypes = response.data;
			}, function(response) {
				$scope.coupTypes ="No coupons were found.";

			});
});
