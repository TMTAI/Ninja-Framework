(function () {

    var app = angular.module("myApp", ['ngMaterial', 'ngResource', 'ngMessages', 'ngMaterialDatePicker', 'ngCookies']);

    app.config(function($mdThemingProvider) {
        $mdThemingProvider.theme('dark-grey').backgroundPalette('grey').dark();
    });

    app.factory('myService', function ($resource, $http, $q) {
        var factory = {

            fetchData: function (URI) {
                $resource.get(URI).$promise.then(
                    function (data) {
                        
                    },
                    function () {
                        
                    }
                );
            }
        }

        return factory;
    });


    app.controller('myController', ['$scope', '$mdDialog' , 'mdcDateTimeDialog', function ($scope, $mdDialog, mdcDateTimeDialog) {
       $scope.times = ["Now", "Custom"];
       $scope.time;

        displayDialog = function displayDialog() {
            mdcDateTimeDialog.show({
                currentDate: moment().startOf('day'),
                showTodaysDate: '',
                time: true,
                shortTime: true,
                format: 'MMMM d, y',
                clickOutsideToClose: true,
            }).then(function (date) {
                $scope.time = date;
            }, function(){

            });
        };


       $scope.getSelectedText = function(){
           if ($scope.time !== "undefined"){
               if ($scope.time === $scope.times[0]){
                    return $scope.select = new Date();
               }else if ($scope.time === $scope.times[1]){
                   return $scope.select = "1321321321321";
               }
           }
       }
    }]);

    app.controller('LoginController', ['$scope', '$cookies', 'myService',function($scope, $cookies, myService){
        $scope.header = "header.ftl.html";
        if ($cookies.get("userInfo") == "undefined"){
            myService.fetchData("/rest/infomation/user").then(
                function (data) {
                    $scope.user = data;
                    $cookies.put("userInfo", JSON.stringify(data));
                },
                function () {

                }
            );
        }else{
            $scope.user = $cookies.getObject("userInfo");
        }
    }]);

    app.controller('UserController', ['$scope', '$cookies', function ($scope, $cookies) {
    }]);
})()


