var exec = require("cordova/exec");

module.exports = {
  insert: function(successCB, errorCB){
    exec(successCB, errorCB, "Contacts", "insert", [[], {}]);
  },

  choose: function(successCB, errorCB){
    exec(successCB, errorCB, "Contacts", "choose", [[], {}]);
  }
};