(function () {

    var app = angular.module("myApp", ['ngMaterial', 'ngResource', 'ngMessages', 'ngCookies']);

    app.config(function ($mdThemingProvider) {
        $mdThemingProvider.theme('dark-grey').backgroundPalette('grey').dark();
    });

    app.run(function ($rootScope, $resource, $location, $cookies, $mdToast, $mdMenu, $mdDialog) {
        $rootScope.isAdmin = false;

        $rootScope.userfullname = "there";


    });

    app.controller('UserController', ['$scope', '$cookies', function ($scope, $cookies) {
    }]);

    app.controller('HomeController', ['$rootScope', '$scope', '$resource', '$mdDialog', '$mdToast', '$cookies', '$location', '$filter', function ($rootScope, $scope, $resource, $mdDialog, $mdToast, $cookies, $location, $filter) {
        $scope.header = "header.ftl.html";
        $rootScope.limit = 80;
        $rootScope.hideAdd = true;

        $rootScope.dashboardNameList = [];

        $rootScope.getDashboardList = function () {
            $resource('/getDashboardList', {
                groups: JSON.stringify($rootScope.userInfo.groups),
                projects: JSON.stringify($rootScope.userInfo.projects)
            }, {
                query: {
                    method: 'post',
                    isArray: true
                }
            }).query().$promise.then(function (respone) {
                $rootScope.dashboardNameList = respone;
                if ($rootScope.dashboardNameList.length != 0) {
                    $rootScope.hideAdd = false;
                }
                if ($location.search().dashboard) {
                    var selectedDashboard = $filter('filter')(respone, {id: $location.search().dashboard}, true)[0];
                    if (selectedDashboard) {
                        $rootScope.currentDashboard = selectedDashboard;
                    } else {
                        $rootScope.currentDashboard = respone[0];
                    }
                } else {
                    $rootScope.currentDashboard = respone[0];
                }

                $rootScope.getting = false;

                if ($rootScope.dashboardNameList.length == 0) {
                    $rootScope.pageName = "Home";
                }
            }, function (error) {
                $mdToast.show(
                    $mdToast.simple()
                        .textContent('Server error')
                        .hideDelay(5000)
                );
                console.log(error);
            });
        }

        $scope.selectedDashboardId = '';
        $scope.dashboardListItemClicked = function (DashboardItem) {

            window.location = "/#/?dashboard=" + DashboardItem.id;

            if (DashboardItem.owner != $rootScope.userInfo.name) {
                $rootScope.hideAdd = true;
            } else {
                $rootScope.hideAdd = false;
            }

            $scope.selectedDashboardId = DashboardItem.id;
            if (!$rootScope.getting) {
                $rootScope.currentDashboard = DashboardItem;
                $rootScope.sonarStList = [];
                $rootScope.reviewList = [];
                $rootScope.showGadget();
            } else {
                $mdToast.show(
                    $mdToast.simple()
                        .textContent('Please wait!')
                        .hideDelay(2000)
                );
            }
        };

        if (typeof $cookies.getObject("userInfo") == "undefined") {
            $resource('/getUserInfo').save().$promise.then(
                function (data) {
                    $rootScope.userInfo = data;
                    $cookies.put("userInfo", JSON.stringify(data));
                    $rootScope.userfullname = $rootScope.userInfo.displayName;
                    $rootScope.name = $rootScope.userInfo.name;

                    $rootScope.isAdmin = $rootScope.userInfo.admin;

                    $rootScope.getDashboardList();

                    $rootScope.getting = false;
                },
                function (error) {
                    $rootScope.getting = false;
                    $rootScope.err = true;
                    console.log(error);
                    // $mdToast.show(
                    //     $mdToast.simple()
                    //         .textContent('Unable to connect to Jira server. Please check connection!')
                    //         .hideDelay(30000)
                    // );
                }
            )
        } else {
            $rootScope.userInfo = $cookies.getObject("userInfo");
            $rootScope.userfullname = $rootScope.userInfo.displayName;
            $rootScope.name = $rootScope.userInfo.name;
            $rootScope.isAdmin = $rootScope.userInfo.admin;
            $rootScope.getDashboardList();
        }

        $rootScope.dashboardOptionClicked = function (DashboardItem, event) {
            $rootScope.dashboardOptionItem = DashboardItem;
            $mdDialog.show({
                templateUrl: 'assets/html/dashboardOptionDialog.html',
                parent: angular.element(document.body),
                targetEvent: event,
                clickOutsideToClose: true
            });
        };


        $rootScope.showGadget = function () {
            if (typeof $rootScope.currentDashboard == "undefined") {
                return;
            }

            $rootScope.sonarStList = [];
            $rootScope.reviewList = [];


            $resource('/getDashboardInfo', {
                id: $rootScope.currentDashboard.id
            }).save().$promise.then(function (respone) {
                console.log(respone);
                $rootScope.dashboardGadgetInfo = respone;
                if (respone.Gadget > 0) {
                    $rootScope.getting = true;
                    $rootScope.hasInfo = true;

                    if (respone.Gadget > 0) {
                        $resource('/showGadgets', {
                            id: $rootScope.currentDashboard.id
                        }).save().$promise.then(function (respone) {
                            //console.log(respone);
                            if (typeof respone.Err != 'undefined') {
                                $resource('/clearSession').save().$promise.then(function (respone) {
                                    //console.log(respone);
                                    window.location = "/login#cookiesexpired";
                                    $cookies.remove("userInfo");
                                });
                            } else {
                                $rootScope.sonarStList = respone.AMSSONARStatisticsGadget;
                                $rootScope.reviewList = respone.AMSOverdueReviewsReportGadget;
                                $rootScope.hasInfo = false;
                            }
                            $rootScope.getting = false;
                        }, function (error) {
                            //console.log(error);
                            $rootScope.hasInfo = false;
                            $rootScope.getting = false;
                            $mdToast.show(
                                $mdToast.simple()
                                    .textContent('Server error! Please logout then login and try again')
                                    .hideDelay(5000)
                            );
                        });
                    }
                }

            }, function (error) {
                //console.log(error);
            });
        };

        $scope.$watch('currentDashboard', function () {
            $rootScope.showGadget();
            for (var i = 0; i < $rootScope.dashboardNameList.length; i++) {
                if ($rootScope.currentDashboard == $rootScope.dashboardNameList[i]) {
                    $rootScope.pageName = $rootScope.dashboardNameList[i].name;
                    break;
                }
            }
        });

        var event;
        $rootScope.showAddWidgetDialog = function (ev) {
            event = ev;

            $mdDialog.show({
                controller: AddWidgetDialogController,
                templateUrl: 'assets/html/choseGadget.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: true
            })
                .then(function (answer) {
                    $scope.status = 'You said the information was "' + answer + '".';
                }, function () {
                    $scope.status = 'You cancelled the dialog.';
                });
        };

        $scope.editReviewGagdget = function (item, event) {
            $rootScope.reviewGadgettoEdit = item;
            $rootScope.gadgetId = item.id;
            //console.log(item);
            $mdDialog.show({
                templateUrl: "assets/html/addNewOverdueReviewReportGadget.html",
                parent: angular.element(document.getElementById('html')),
                targetEvent: event,
                clickOutsideToClose: false
            });
        };

        $scope.deleteGagdget = function (item) {
            $rootScope.getting = true;
            $resource('/deleteGadget', {
                GadgetId: item.id
            }).save().$promise.then(function (respone) {
                //console.log(respone);
                $rootScope.showGadget();
                $rootScope.getting = false;
            }, function (error) {
                //console.log(error);
                $rootScope.getting = false;
            });

            //console.log("Delete " + item.id);
        };

        $scope.editSonarGagdget = function (item, event) {
            $rootScope.gadgetId = item.id;
            $rootScope.sonarGadgettoEdit = item;
            $scope.periodForSonarDashboard = $scope.period;
            $mdDialog.show({
                templateUrl: "assets/html/addNewSonarGadget.html",
                parent: angular.element(document.getElementById('html')),
                targetEvent: event,
                clickOutsideToClose: false
            });
        };

        $scope.updateViewsGadget = function () {
            $rootScope.getting = true;
            $rootScope.showGadget();
        }

        function AddWidgetDialogController($resource, $scope, $rootScope, $mdDialog) {
            $rootScope.gadgetId = "";

            $resource('/getGadgetList', {}, {
                query: {
                    method: 'post',
                    isArray: true
                }
            }).query().$promise.then(function (respone) {
                $scope.gadgetList = respone;
                //console.log(respone);
            }, function (error) {
                $mdToast.show(
                    $mdToast.simple()
                        .textContent('Server error')
                        .hideDelay(5000)
                );
                //console.log(error);
            });

            $scope.addGadget = function (item) {
                $mdDialog.cancel();
                //clear cache
                $rootScope.gadgetToEdit = null;
                //console.log(item.name);
                $rootScope.gadgetType = item.type;

                $mdDialog.show({
                    templateUrl: item.addnewUIurl,
                    parent: angular.element(document.getElementById('html')),
                    targetEvent: event,
                    clickOutsideToClose: false
                });
            };

            $scope.click = function () {

            }

            $scope.hide = function () {
                $mdDialog.hide();
            };

            $scope.cancel = function () {
                $mdDialog.cancel();
            };

            $scope.answer = function (answer) {
                $mdDialog.hide(answer);
            };

            ///////////////////////SonarGadget/////////////////////////

            $rootScope.check = function (val) {
                return val >= 0;
            };
        }
    }]);

    app.controller('HeaderCtrl', function ($rootScope, $scope, $resource, $mdDialog, $mdToast, $cookies) {

        function DialogController($scope, $mdDialog) {
            $scope.cancel = function () {
                $mdDialog.cancel();
            };
        }

        $scope.showAddNewDialog = function (ev) {
            $mdDialog.show({
                controller: DialogController,
                templateUrl: 'assets/html/addNewDashboard.html',
                parent: angular.element(document.html),
                targetEvent: ev,
                clickOutsideToClose: true
            })
                .then(function (answer) {
                }, function () {
                });
        };

        $scope.openConfigurationMenu = function ($mdOpenMenu, ev) {
            $mdOpenMenu(ev);
        };

        $scope.clearCookies = function () {
            if ($cookies.getObject("userInfo") != "undefined"){
                $cookies.remove("userInfo");
            }
        }
    });

    app.controller('dasboardOptionController', ['$scope', '$rootScope', '$mdDialog', '$mdToast', '$location', '$resource', '$cookies', function ($scope, $rootScope, $mdDialog, $mdToast, $location, $resource, $cookies) {
        $scope.dashboardName = $rootScope.dashboardOptionItem.name;

        $scope.cancel = function () {
            $mdDialog.cancel();
        };

        $resource('/getProjectList', {}, {
            query: {
                method: 'post',
                isArray: true
            }
        }).query().$promise.then(function (data) {
            $scope.projects = data;
            //console.log(data);
        }, function (error) {
            //console.log(error);
            $mdToast.show(
                $mdToast.simple()
                    .textContent('Error! Can not get project list. Please check connection!')
                    .hideDelay(10000)
            );
        });

        $scope.groups = $rootScope.userInfo.groups;
        var tempShare;
        if ($rootScope.dashboardOptionItem.privacy.status == "private") {
            $scope.share = [];
            $scope.share.push("Only me");
        } else if ($rootScope.dashboardOptionItem.privacy.status == "public") {
            $scope.share = [];
            $scope.share.push("Everyone");
        } else {
            $scope.share = $rootScope.dashboardOptionItem.privacy.share;
        }

        $scope.groupItemClicked = function (groupItem) {

            if ($scope.share.indexOf("Ony me") > -1) {
                $scope.share.splice($scope.share.indexOf("Ony me"), 1);
            }

            if ($scope.share.indexOf("Everyone") > -1) {
                $scope.share.splice($scope.share.indexOf("Everyone"), 1);
            }

            if ($scope.share.indexOf(groupItem) < 0) {
                $scope.share.push(groupItem);
            }
            //console.log($scope.share);

        };

        $scope.projectItemClicked = function (projectItem) {

            if ($scope.share.indexOf("Ony me") > -1) {
                $scope.share.splice($scope.share.indexOf("Ony me"), 1);
            }

            if ($scope.share.indexOf("Everyone") > -1) {
                $scope.share.splice($scope.share.indexOf("Everyone"), 1);
            }

            if ($scope.share.indexOf(projectItem) < 0) {
                $scope.share.push(projectItem);
            }
            //console.log($scope.share);
        };

        $scope.everyoneClicked = function () {
            $scope.share = [];
            $scope.share.push("Everyone");
            tempShare = $scope.share;
            //console.log($scope.share);
        };

        $scope.privateClicked = function () {
            $scope.share = [];
            $scope.share.push("Ony me");
            tempShare = $scope.share;
            //console.log($scope.share);
        };

        $scope.allGroupClicked = function () {
            $scope.share = [];
            $scope.share = $scope.groups;
            tempShare = $scope.share;
            //console.log($scope.share);
        };
        $scope.allProjectClicked = function () {
            $scope.share = [];
            $scope.share = $scope.projects;
            tempShare = $scope.share;
            //console.log($scope.share);
        };

        $scope.saveOption = function () {
            var status = "other";
            if ($scope.share == "") {
                status = "private";
            } else if ($scope.share.indexOf("Ony me") > -1) {
                status = "private";
            } else {
                if ($scope.share.indexOf("Everyone") > -1) {
                    status = "public";
                }
            }

            $resource('/updateDashboardOption', {
                id: $rootScope.dashboardOptionItem.id,
                name: $scope.dashboardName,
                privacy: {
                    status: status,
                    share: $scope.share
                }
            }).save().$promise.then(function (data) {
                $rootScope.getDashboardList();
                //console.log(data);
                $scope.cancel();
            }, function (error) {
                //console.log(error);
                $mdToast.show(
                    $mdToast.simple()
                        .textContent('Error! Can not update dashboard option')
                        .hideDelay(5000)
                );
            });
        };

        $scope.deleteDashboard = function () {
            $resource('/deleteDashboard', {
                id: $rootScope.dashboardOptionItem.id
            }).save().$promise.then(function (respone) {
                $rootScope.getDashboardList();
                $scope.cancel();
                //console.log(respone);
            }, function (error) {
                $mdToast.show(
                    $mdToast.simple()
                        .textContent('Server error')
                        .hideDelay(5000)
                );
                //console.log(error);
            });
        }
    }]);

    app.controller('AddNewOverdueReviewReportGadgetCtrl', function ($scope, $rootScope, $window, $mdDialog, $mdToast, $location, $resource) {

        $scope.choseProjectPage = true;
        $scope.choseUserPage = false;
        $scope.Loading = false;

        $scope.CruProjectList = [];
        $scope.projectId = "";
        $scope.IAItems = [];
        $scope.IAItemNames = [];
        $scope.selectedIA = [];
        $scope.loginForm = false;

        $scope.loginCru = function (cruusername, crupassword) {
            $scope.Loading = true;
            $resource('/loginCru', {
                username: cruusername,
                password: crupassword
            }).save().$promise.then(function (response) {
                $scope.loginForm = false;
                $scope.getCruProjectList();
                $scope.Loading = false;

            }, function (error) {
                $scope.Loading = false;
                $scope.loginForm = true;
                $mdToast.show(
                    $mdToast.simple()
                        .textContent('Error! Please check connection!')
                        .hideDelay(10000)
                );
            });

        };


        $scope.cancel = function () {
            $mdDialog.cancel();
        };

        $scope.getCruProjectList = function () {
            $scope.Loading = true;
            $resource('/getCruProjectList', {}, {
                query: {
                    method: 'post',
                    isArray: true
                }
            }).query().$promise.then(function (respone) {
                // console.log(respone);
                $scope.CruProjectList = respone;


                if ($scope.CruProjectList.length == 0) {
                    $scope.loginForm = true;
                }
                if ($rootScope.gadgetId != "") {
                    projectId = $rootScope.reviewGadgettoEdit.project;
                }


                $scope.Loading = false;
            }, function (error) {
                $scope.Loading = false;
                $scope.loginForm = true;
                $mdToast.show(
                    $mdToast.simple()
                        .textContent('Error! Can not get Cru Project List. Please login to Crucible!')
                        .hideDelay(10000)
                );
            });
        };

        $scope.getCruProjectList();

        $scope.choseProjectNext = function (projectId) {
            if (projectId == "") {
                $mdToast.show(
                    $mdToast.simple()
                        .textContent('Please choose a project')
                        .hideDelay(5000)
                );
            } else {


                if ($rootScope.gadgetId != "") {
                    var data = {
                        DashboardId: $rootScope.currentDashboard.id,
                        DashboardGadgetId: $rootScope.gadgetId,
                        Data: {
                            Project: projectId
                        }
                    };


                    $resource('/updateGadget', {
                        data: data
                    }, {
                        query: {
                            method: 'post',
                            isArray: false
                        }
                    }).query().$promise.then(function (data) {
                        //console.log(data);
                        $mdDialog.cancel();
                        $rootScope.showGadget();
                    }, function (error) {
                        //console.log(error);
                        $mdToast.show(
                            $mdToast.simple()
                                .textContent('Error! Can not update gadget. ')
                                .hideDelay(10000)
                        );
                    });

                } else {
                    var data = {
                        DashboardId: $rootScope.currentDashboard.id,
                        GadgetType: $rootScope.gadgetType,
                        Data: {
                            Project: projectId
                        }
                    };


                    $resource('/addNewGadget', {
                        data: data
                    }, {
                        query: {
                            method: 'post',
                            isArray: false
                        }
                    }).query().$promise.then(function (data) {
                        //console.log(data);
                        $mdDialog.cancel();
                        $rootScope.showGadget();
                    }, function (error) {
                        //console.log(error);
                        $mdToast.show(
                            $mdToast.simple()
                                .textContent('Error! Can not get add new gadget.')
                                .hideDelay(5000)
                        );
                    });
                }
            }
            $rootScope.gadgetId = "";
        };
    });

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    app.controller('AddNewSonarGadgetCtrl', function ($scope, $rootScope, $window, $mdDialog, $mdToast, $location, $resource) {
        $scope.choseReleasePage = true;
        $scope.choseIAPage = false;
        $scope.choseMetricPage = false;
        $scope.IALoading = false;

        $scope.releaseName = "";
        $scope.releaseUrl = "";
        $scope.IAItems = [];
        $scope.IAItemNames = [];
        $scope.selectedIA = [];
        $scope.selectedMetric = [];
        $scope.period = "";

        $scope.cancel = function () {
            $mdDialog.cancel();
        };


        $resource('/getReleaseList',
            {},
            { query :{
                method: 'post',
                isArray: true
            }
            }).query().$promise.then(function (data) {
            $scope.ReleaseList = data;
            //console.log(data);
            if ($rootScope.gadgetId != "") {
                $scope.releaseName = $rootScope.sonarGadgettoEdit.release;
            } else {
                $scope.releaseName = $scope.ReleaseList[0].name;
            }

        }, function (error) {
            //console.log(error);
            $mdToast.show(
                $mdToast.simple()
                    .textContent('Error! Can not get Release List. Please check connection!')
                    .hideDelay(10000)
            );
        });


        $resource('/getPeriodList', {}, {
            query: {
                method: 'post',
                isArray: false
            }
        }).query().$promise.then(function (respone) {
            //console.log(respone);
            $scope.periodList = respone.PeriodArray;
            if ($rootScope.gadgetId != "") {
                if($rootScope.sonarGadgettoEdit.period) {
                    $scope.periodForSonarDashboard = $rootScope.sonarGadgettoEdit.period;
                } else {
                    $scope.periodForSonarDashboard = respone.CurrentPeriod;
                }
            } else {
                $scope.periodForSonarDashboard = respone.CurrentPeriod;
            }
        }, function (error) {

        });


        $scope.choseReleaseNext = function () {

            for (var i = 0; i < $scope.ReleaseList.length; i++) {
                if ($scope.ReleaseList[i].name == $scope.releaseName) {
                    $scope.releaseUrl = $scope.ReleaseList[i].url;
                    break;
                }
            }

            $scope.IALoading = true;
            $resource('/getIAComponents', {
                data: {
                    url: $scope.releaseUrl
                }
            }, {
                query: {
                    method: 'post',
                    isArray: true
                }
            }).query().$promise.then(function (respone) {
                //console.log(respone);
                $scope.IAItems = respone;
                for (var i = 0; i < respone.length; i++) {
                    $scope.IAItemNames.push(respone[i].name);
                }

                if ($rootScope.gadgetId != "") {
                    for (var i = 0; i < $rootScope.sonarGadgettoEdit.RsIAArray.length; i++) {
                        $scope.selectedIA.push($rootScope.sonarGadgettoEdit.RsIAArray[i].name);
                    }
                } else {
                    $scope.selectedIA.push(respone[0].name);
                }


                $scope.choseReleasePage = false;
                $scope.choseIAPage = true;
                $scope.IALoading = false;
            }, function (error) {
                //console.log(error);
                $mdToast.show(
                    $mdToast.simple()
                        .textContent('Error! Can not get IA Component. Please check connection!')
                        .hideDelay(10000)
                );
            });
        };


        $scope.toggleIA = function (item) {

            var idx = $scope.selectedIA.indexOf(item);
            if (idx > -1) {
                $scope.selectedIA.splice(idx, 1);
            } else {
                $scope.selectedIA.push(item);
            }

            //console.log($scope.selectedIA);
        };

        $scope.existsIA = function (item) {
            return $scope.selectedIA.indexOf(item) > -1;
        };

        $scope.isIAIndeterminate = function () {
            return ($scope.selectedIA.length !== 0 &&
                $scope.selectedIA.length !== $scope.IAItemNames.length);
        };

        $scope.isIAChecked = function () {
            return $scope.selectedIA.length === $scope.IAItemNames.length;
        };

        $scope.toggleAllIA = function () {
            if ($scope.selectedIA.length === $scope.IAItemNames.length) {
                $scope.selectedIA = [];
            } else if ($scope.selectedIA.length === 0 || $scope.selectedIA.length > 0) {
                $scope.selectedIA = $scope.IAItemNames.slice(0);
            }
        };

        $scope.choseIANext = function () {
            $scope.IALoading = true;
            $scope.components = [];
            $scope.IALoading = false;
            $scope.choseMetricPage = true;
            $scope.choseIAPage = false;

            //console.log($scope.selectedIA);


        };


        $scope.choseIABack = function () {
            $scope.choseReleasePage = true;
            $scope.choseIAPage = false;
        };


        /////////////////////////////////////////////////////////////////////////////////////////

        $scope.itemsNameMetric = [];
        $scope.MetricItems = [];
        $resource('/getMetrics',
            {},
            { query :{
            method: 'post',
            isArray: true
            }
        }).query().$promise.then(function (data) {
            //console.log(data);
            $scope.MetricItems = data;
            for (var i = 0; i < data.length; i++) {
                $scope.itemsNameMetric.push(data[i].name);
            }


            if ($rootScope.gadgetId != "") {
                for (var i = 0; i < $rootScope.sonarGadgettoEdit.metricList.length; i++) {
                    $scope.selectedMetric.push($rootScope.sonarGadgettoEdit.metricList[i].name);
                }
            } else {
                $scope.selectedMetric.push($scope.MetricItems[0].name);
            }


        }, function (error) {
            //console.log(error);
            $mdToast.show(
                $mdToast.simple()
                    .textContent('Error! Can not get Metric List. Please check connection!')
                    .hideDelay(10000)
            );
        });


        $scope.toggleMetric = function (item) {
            var idx = $scope.selectedMetric.indexOf(item);
            if (idx > -1) {
                $scope.selectedMetric.splice(idx, 1);
            } else {
                $scope.selectedMetric.push(item);
            }
        };

        $scope.existsMetric = function (item) {
            return $scope.selectedMetric.indexOf(item) > -1;
        };

        $scope.isMetricIndeterminate = function () {
            return ($scope.selectedMetric.length !== 0 &&
                $scope.selectedMetric.length !== $scope.itemsNameMetric.length);
        };

        $scope.isMetricChecked = function () {
            return $scope.selectedMetric.length === $scope.itemsNameMetric.length;
        };

        $scope.toggleAllMetric = function () {
            if ($scope.selectedMetric.length === $scope.itemsNameMetric.length) {
                $scope.selectedMetric = [];
            } else if ($scope.selectedMetric.length === 0 || $scope.selectedMetric.length > 0) {
                $scope.selectedMetric = $scope.itemsNameMetric.slice(0);
            }
        };

        /////////////////////////////////////////////////////////////////////////////////////////

        $scope.chosePeriodBack = function() {
            $scope.choseMetricPage = true;
            $scope.chosePeriodPage = false;
        }

        $scope.chosePeriodNext = function() {
            //console.log("choseMetricNext");
            if ($scope.selectedMetric.length == 0) {
                $scope.selectedMetric.push($scope.MetricItems[0].name);
            }
            $scope.selectedMetricKeys = [];

            for (var i = 0; i < $scope.selectedMetric.length; i++) {
                for (var j = 0; j < $scope.MetricItems.length; j++) {
                    if ($scope.selectedMetric[i] == $scope.MetricItems[j].name) {
                        $scope.selectedMetricKeys.push($scope.MetricItems[j].key);
                    }
                }
            }

            finish();
        }

        $scope.choseMetricBack = function () {
            $scope.choseMetricPage = false;
            $scope.choseIAPage = true;
        };


        $scope.choseMetricNext = function () {
            $scope.choseMetricPage = false;
            $scope.chosePeriodPage = true;
        };

        function finish() {
            var IANames = "";
            for (var i = 0; i < $scope.selectedIA.length; i++) {
                if (i < $scope.selectedIA.length - 1) {
                    IANames = IANames + $scope.selectedIA[i] + ",";
                } else {
                    IANames = IANames + $scope.selectedIA[i];
                }
            }

            var Metrics = "";
            for (var i = 0; i < $scope.selectedMetricKeys.length; i++) {
                if (i < $scope.selectedMetricKeys.length - 1) {
                    Metrics = Metrics + $scope.selectedMetricKeys[i] + ",";
                } else {
                    Metrics = Metrics + $scope.selectedMetricKeys[i];
                }
            }


            if ($rootScope.gadgetId != "") {

                var data = {
                    DashboardId: $rootScope.currentDashboard.id,
                    DashboardGadgetId: $rootScope.gadgetId,
                    Data: {
                        Release: $scope.releaseName,
                        IANames: IANames,
                        Metrics: Metrics,
                        period: $scope.periodForSonarDashboard
                    }
                };


                //console.log(data);
                $resource('/updateGadget', {
                    data: data
                }, {
                    query: {
                        method: 'post',
                        isArray: false
                    }
                }).query().$promise.then(function (data) {
                    //console.log(data);
                    $mdDialog.cancel();
                    $rootScope.showGadget();
                }, function (error) {
                    //console.log(error);
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent('Error! Can not update gadget. ')
                            .hideDelay(10000)
                    );
                });
            } else {

                var data = {
                    DashboardId: $rootScope.currentDashboard.id,
                    GadgetType: $rootScope.gadgetType,
                    Data: {
                        Release: $scope.releaseName,
                        IANames: IANames,
                        Metrics: Metrics,
                        period: $scope.periodForSonarDashboard
                    }
                };


                //console.log(data);
                $resource('/addNewGadget', {
                    data: data
                }, {
                    query: {
                        method: 'post',
                        isArray: false
                    }
                }).query().$promise.then(function (data) {
                    //console.log(data);
                    $mdDialog.cancel();
                    $rootScope.showGadget();
                }, function (error) {
                    //console.log(error);
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent('Error! Can not get add new gadget')
                            .hideDelay(10000)
                    );
                });

            }
            $rootScope.gadgetId = "";
        }
    });
})()


