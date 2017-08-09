var app = angular.module('myApp', []);
app
		.controller(
				'myController',
				function($scope, $http) {

					$scope.refreshTables = function() {
						$scope.getAllComps();
						$scope.getAllCusts();

					}

					$scope.resetFields = function() {
						$scope.customer.name = "";
						$scope.customer.password = "";
						$scope.company.id = null;
						$scope.company.name = null;
						$scope.company.email = null;
						$scope.company.password = null;
						$scope.customer.id = null;
						$scope.customer.name = null;
						$scope.customer.password = null;
						$scope.companyId= null;
						$scope.customerId = null;
						$scope.NoCompany="";
						$scope.NoCustomer = "";
						
						

					}
					
					$scope.findCompany = function(id) {
						var gotCompany = [ {id : "",companyName : "",companyEmail : ""} ];
						$scope.gotCompany = gotCompany;
						$http.get("rest/adminWs/findCompany/" + id).then(
								function(response) {
									$scope.gotCompany = response.data;
								}, function(response) {
									$scope.NoCompany = "could not be found in the system.";
								});
					}

					$scope.findCustomer = function(id) {
						var gotCustomer = [ {id : "",customerName : ""} ];
						$scope.gotCustomer = gotCustomer;
						$http.get("rest/adminWs/findCustomer/" + id).then(
								function(response) {
									$scope.gotCustomer = response.data;
								}, function(response) {
									$scope.NoCustomer = "could not be found  in the system.";

								});
					}

					$scope.getAllComps = function() {
						$scope.companies = $http
								.get("rest/adminWs/getAllCompanies")
								.then(
										function(response) {
											$scope.companies = response.data;
											$scope.sortColumn = "companyName";
																					},
										function(response) {
											$scope.companies = "The company List is empty.";

										});
					}

					$scope.getAllCusts = function() {
						$scope.companies = $http
								.get("rest/adminWs/getAllCustomers")
								.then(
										function(response) {
											$scope.customers = response.data;
											$scope.sortColumn = "customerName";
									
										},
										function(response) {
											$scope.customers = "The company List is empty.";


										});
					}

					$scope.deleteCompany = function(id) {
						$http
								.get("rest/adminWs/delComp/" + id)
								.then(
										function(response) {
											$scope.deletedCompany = "The company you requested to delete (id:"+ id+ ") has been removed successfully.";
											$scope.refreshTables();
											$scope.resetFields();
										},
										function(response) {
											$scope.deletedCompany =  "The company (id:"+ id+ ") you requested to remove could not be found and was not deleted.";
										});
					}

					// DELETE CUSTOMER
					$scope.deleteCustomer = function(id) {
						$http
								.get("rest/adminWs/delCust/" + id)
								.then(
										function(response) {
											$scope.refreshTables();
											$scope.resetFields();
											$scope.deletedCust = "The customer you requested to delete (id:"+ id+ ") has been removed successfully.";
										},
										function(response) {
											$scope.deletedCust =  "The customer (id:"+ id+ ") you requested to remove could not be found and was not deleted.";

										});
					}

					$scope.doUpdate = function(id, email, password) {
						$http
								.get(
										"rest/adminWs/updateComp/" + id + "/"
												+ email + "/" + password)
								.then(
										function(response) {
											$scope.updatedComp ="The company`s email and password have been updated successfully (new email: "+email+", new password:"+password+")"
											$scope.refreshTables();
											$scope.resetFields();
										},
										function(response) {
											$scope.updatedComp = "The company (id:"+ id+ ") you requested to update could not be found and was not updated.";


										});
					}

					// UPDATE CUSTOMER
					$scope.doUpdateCust = function(id, password) {
						$http
								.get(
										"rest/adminWs/updateCust/" + id + "/"
												+ password)
								.then(
										function(response) {
											$scope.updatedCust ="The customer`s password has been updated successfully (new password:"+password+".)"
											$scope.refreshTables();
											$scope.resetFields();

										},
										function(response) {
											$scope.updatedCust = "The customer (id:"+ id+ ") you requested to update could not be found and was not updated.";

										});
					}
					

				

					$scope.quit = function() {
						$http.get("rest/adminWs/quit").then(function(response) {
							$scope.goodResponse = response.headers()['quit'];

						});
					}
					var userNamefromSession = "";
					$http
							.get("rest/adminWs/getSessionUser")
							.then(
									function(response) {
										$scope.userNamefromSession = response
												.headers()['user'];
									},
									function(response) {
										alert("error getting user");
									});

					$scope.createComp = function(name, email, password) {
						if ($scope.createCompForm.$valid) {
							$http
									.get("rest/adminWs/createComp/" + name	+ "/" + email + "/"	+ password)
									.then(
											function(response) {
												$scope.createdCompany = "The company you requested has been created successfully."
												$scope.resetFields();
											},
											function(response) {
												$scope.createdCompany = "An error occurred and the company could not be created."

											});
						}

					};

					$scope.createCust = function(name, password) {
						if ($scope.createCustForm.$valid) {
							$http
									.get(
											"rest/adminWs/createCust/" + name
													+ "/" + password)
									.then(
											function(response) {
												$scope.createdCustomer = "The customer you requested ("+name+","+ password+") has been created successfully."
												$scope.resetFields();
											},
											function(response) {
												$scope.createdCustomer = "An error occurred and the customer could not be created."

											
											});
						}

					};

				});
