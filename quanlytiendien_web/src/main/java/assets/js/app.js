(function () {

    var app = angular.module("myApp", ['ngMaterial', 'ngResource', 'ngMessages']);

    app.config(function($mdThemingProvider) {
        $mdThemingProvider.theme('dark-grey').backgroundPalette('grey').dark();
    });

})()


