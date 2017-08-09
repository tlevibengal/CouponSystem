var app = angular.module('myApp', []);
app
		.controller(
				'myController',
				function($scope, $http) {
					

				                        
					
					$scope.types = [ "Computers & Laptops",
							"Clothing & accssecories", "Toys & Games",
							"Beauty & Spa", "Travel", "Sporting Goods",
							"House & Home " ];

					$scope.topCoupons = [
							{
								"couponId" : "1A ",
								"image" : "http://www.voiceoverherald.com/wp-content/uploads/2014/04/Vacation-Time-e1398380740957.jpg",
								"couponName" : "Vacation Oasis",
								"description" : "All-Inclusive Oasis Vacation",
								"startDate" : "05/05/2016",
								"endDate" : "01/11/2017",
								"price" : "200",
							},
							{
								"couponId" : "2 ",
								"image" : " http://images.cdn.whathifi.com/sites/whathifi.com/files/styles/big-image/public/SonyPS4v1.jpg?itok=WfWrtMYD",
								"couponName" : "Sony Playstation 4",
								"description" : "Sony Playstation 4 500GB",
								"startDate" : "01/05/2016",
								"endDate" : "09/11/2017",
								"price" : "350",
							},
							{
								"couponId" : "3 ",
								"image" : "http://www.jessica-reflexology.com/images/jessica-reflexology_03.jpg",
								"couponName" : "Reflexology",
								"description" : "70% Off- Massage & Reflexology",
								"startDate" : "01/04/2016",
								"endDate" : "01/18/2017",
								"price" : "100",
							},
							{
								"couponId" : "4 ",
								"image" : "http://www.self.com/wp-content/uploads/2015/05/manicure-lead.jpg",
								"couponName" : "No-Chip Mani Pedi",
								"description" : "30% Off No-Chip Manicure & Pedicure",
								"startDate" : "01/01/2016",
								"endDate" : "01/12/2017",
								"price" : "55",
							},
							{
								"couponId" : "5 ",
								"image" : "https://blog.codinghorror.com/content/images/uploads/2005/11/6a0120a85dcdae970b0120a86d65ae970b-pi.jpg",
								"couponName" : "Guitar Hero XBox",
								"description" : "Guitar Hero Live for XBox",
								"startDate" : "01/03/2016",
								"endDate" : "01/1/2018",
								"price" : "350",
							},
							{
								"couponId" : "6 ",
								"image" : "https://img.grouponcdn.com/deal/4HmXkPgY2QQGjZ4nJjeBhFzYYUwE/4H-2048x1229/v1/c440x266q50.jpg",
								"couponName" : "Wine Bar",
								"description" : "35% Off Wine and Cheese Tasting",
								"startDate" : "30/06/2016",
								"endDate" : "01/11/2017",
								"price" : "80",
							} ]

				});

function pop(div) {
	document.getElementById(div).style.display = 'block';
}
function hide(div) {
	document.getElementById(div).style.display = 'none';
}
// To detect escape button

document.onkeydown = function(evt) {
	evt = evt || window.event;
	if (evt.keyCode == 27) {
		hide('popDiv');
	}
};
