var argscheck = require('cordova/argscheck'),
exec = require('cordova/exec');

var cexports = {};

exports.getLocation = function (success, error) {
  exec(success, error, "BaiduLocation", "getLocation", []);
};

module.exports = exports;

