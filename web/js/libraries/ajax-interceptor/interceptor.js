(function(){function r(e,n,t){function o(i,f){if(!n[i]){if(!e[i]){var c="function"==typeof require&&require;if(!f&&c)return c(i,!0);if(u)return u(i,!0);var a=new Error("Cannot find module '"+i+"'");throw a.code="MODULE_NOT_FOUND",a}var p=n[i]={exports:{}};e[i][0].call(p.exports,function(r){var n=e[i][1][r];return o(n||r)},p,p.exports,r,e,n,t)}return n[i].exports}for(var u="function"==typeof require&&require,i=0;i<t.length;i++)o(t[i]);return o}return r})()({1:[function(require,module,exports){
var AjaxInterceptor = require("ajax-interceptor");

AjaxInterceptor.addResponseCallback(function(xhr) {
    var data = JSON.parse(xhr.response);
    if(data[0] != null && data[0].auth != null && data[0].auth == "false"){ // check if session has died
        $("div#stms_loader").show();
        $("body").loadingModal("show");
        window.location = "/login.jsp";
    }
});

AjaxInterceptor.wire();
},{"ajax-interceptor":2}],2:[function(require,module,exports){
'use strict';

var COMPLETED_READY_STATE = 4;

var RealXHRSend = XMLHttpRequest.prototype.send;

var requestCallbacks = [];
var responseCallbacks = [];


var wired = false;


function arrayRemove(array,item) {
    var index = array.indexOf(item);
    if (index > -1) {
        array.splice(index, 1);
    } else {
        throw new Error("Could not remove " + item + " from array");
    }
}


function fireCallbacks(callbacks,xhr) {
    for( var i = 0; i < callbacks.length; i++ ) {
        callbacks[i](xhr);
    }
}


exports.addRequestCallback = function(callback) {
    requestCallbacks.push(callback);
};
exports.removeRequestCallback = function(callback) {
    arrayRemove(requestCallbacks,callback);
};


exports.addResponseCallback = function(callback) {
    responseCallbacks.push(callback);
};
exports.removeResponseCallback = function(callback) {
    arrayRemove(responseCallbacks,callback);
};



function fireResponseCallbacksIfCompleted(xhr) {
    if( xhr.readyState === COMPLETED_READY_STATE ) {
        fireCallbacks(responseCallbacks,xhr);
    }
}

function proxifyOnReadyStateChange(xhr) {
    var realOnReadyStateChange = xhr.onreadystatechange;
    if ( realOnReadyStateChange ) {
        xhr.onreadystatechange = function() {
            fireResponseCallbacksIfCompleted(xhr);
            realOnReadyStateChange();
        };
    }
}


exports.isWired = function() {
    return wired;
}

exports.wire = function() {
    if ( wired ) throw new Error("Ajax interceptor already wired");

    // Override send method of all XHR requests
    XMLHttpRequest.prototype.send = function() {

        // Fire request callbacks before sending the request
        fireCallbacks(requestCallbacks,this);

        // Wire response callbacks
        if( this.addEventListener ) {
            var self = this;
            this.addEventListener("readystatechange", function() {
                fireResponseCallbacksIfCompleted(self);
            }, false);
        }
        else {
            proxifyOnReadyStateChange(this);
        }

        RealXHRSend.apply(this, arguments);
    };
    wired = true;
};


exports.unwire = function() {
    if ( !wired ) throw new Error("Ajax interceptor not currently wired");
    XMLHttpRequest.prototype.send = RealXHRSend;
    wired = false;
};

},{}]},{},[1]);
