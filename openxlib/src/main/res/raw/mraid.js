(function () {
  var mraid = window.mraid = {};
  mraid.eventListeners = {};
  mraid.state = "loading";
  mraid.viewable = false;
  mraid.resizePropertiesInitialized = false;
  
  var logConsole = 0;

  mraid.orientationProperties = {
      allowOrientationChange: true,
      forceOrientation: "none"
  };

  mraid.expandProperties = {
      width: 0,
      height: 0,
      useCustomClose: false
  };

  
  mraid.resizeProperties = {
      width: 0,
      height: 0,
      customClosePosition: "top-right",
      offsetX: 0,
      offsetY: 0,
      allowOffscreen: true
  };

  var safeString = function (str) {
      return str ? str : '';
  };

  
  var callContainer = function (command) {
      
      var args = Array.prototype.slice.call(arguments);
      args.shift();

      jsBridge[command].apply(jsBridge, args);

  };

  var log = function (str) {
      if (logConsole){
          console.log(str);
      }
  };

 
  mraid.open = function (url) {
      log('mraid.open(' + url + ')');
      
      callContainer('open', url);
  };

 
  mraid.resize = function () {
      
      if (mraid.resizePropertiesInitialized) {
          
          callContainer('resize');
          
      } else {
          
          mraid.onError("[width] and [height] parameters must be at least 1px. Set them using setResizeProperties() MRAID method.", "resize");
      }
  };

  mraid.expand = function (url) {
      log('mraid.expand(' + url + ')');
      
      if (url) {
          callContainer('expand', url);
      } else {
          callContainer('expand');
      }
  };


  mraid.close = function () {
      callContainer('close');
  };


  mraid.getPlacementType = function () {
     
      return jsBridge.getPlacementType();
  };


  mraid.getState = function () {
      
      return mraid.state;
  };


  mraid.getVersion = function () {
      return '2.0';
  };


  mraid.isViewable = function () {

      
      return mraid.viewable;
  };


  mraid.getScreenSize = function () {
      
      return JSON.parse(jsBridge.getScreenSize());
  };


  mraid.getDefaultPosition = function () {
      
      return JSON.parse(jsBridge.getDefaultPosition());
  };


  mraid.getCurrentPosition = function () {
      
      return JSON.parse(jsBridge.getCurrentPosition());
  };


  mraid.getMaxSize = function () {

      return JSON.parse(jsBridge.getMaxSize());
  };


  mraid.supports = function (feature) {

      return mraid.allSupports[feature];

  };


  mraid.playVideo = function (url) {
      callContainer('playVideo', url);
  };


  mraid.addEventListener = function (event, listener) {
      
      var handlers = mraid.eventListeners[event];
      if (handlers == null) { 
          handlers = mraid.eventListeners[event] = [];
      }

      
      for (var handler = 0; handler < handlers.length; handler++) {
          if (listener == handlers[handler]) { 
              return;
          }
      }

      
      handlers.push(listener);
  };


  mraid.removeEventListener = function (event, listener) {
      
      var handlers = mraid.eventListeners[event];
      if (handlers != null) {
          for (var handler = 0; handler < handlers.length; handler++) {
              if (handlers[handler] == listener) {
                  handlers.splice(handler, 1);
                  break;
              }
          }
      }
  };

  
  mraid.setOrientationProperties = function (properties) {
      if (!properties)
          return;
      var aoc = properties.allowOrientationChange;
      if (aoc === true || aoc === false) {
          mraid.orientationProperties.allowOrientationChange = aoc;
      }

      var fo = properties.forceOrientation;
      if (fo == 'landscape' || fo == 'portrait' || fo == 'none') {
          mraid.orientationProperties.forceOrientation = fo;
      }

      callContainer('onOrientationPropertiesChanged', JSON.stringify(mraid.getOrientationProperties()));
  };

  
  mraid.getOrientationProperties = function () {
      return mraid.orientationProperties;
  };

  
  mraid.getResizeProperties = function () {
      return mraid.resizeProperties;
  };

  
  mraid.getExpandProperties = function () {
     
      mraid.expandProperties.isModal = true;
      return mraid.expandProperties;
  };

 
  mraid.setResizeProperties = function (properties) {
      
      if (properties && properties.width != null && typeof properties.width !== 'undefined' && !isNaN(properties.width)) {
          mraid.resizeProperties.width = properties.width;
      }

      if (properties && properties.height != null && typeof properties.height !== 'undefined' && !isNaN(properties.height)) {
          mraid.resizeProperties.height = properties.height;
      }

      if (properties && properties.customClosePosition != null && typeof properties.customClosePosition !== 'undefined' && isNaN(properties.customClosePosition)) {
          mraid.resizeProperties.customClosePosition = properties.customClosePosition;
      }

      if (properties && properties.offsetX != null && typeof properties.offsetX !== 'undefined' && !isNaN(properties.offsetX)) {
          mraid.resizeProperties.offsetX = properties.offsetX;
      }

      if (properties && properties.offsetY != null && typeof properties.offsetY !== 'undefined' && !isNaN(properties.offsetY)) {
          mraid.resizeProperties.offsetY = properties.offsetY;
      }

      if (properties && properties.allowOffscreen == true || properties.allowOffscreen == false) {
          mraid.resizeProperties.allowOffscreen = properties.allowOffscreen;
      }

      mraid.resizePropertiesInitialized = mraid.resizeProperties.width > 0 && mraid.resizeProperties.height > 0;
  };

 
  mraid.setExpandProperties = function (properties) {

      

      if (properties && properties.width != null && typeof properties.width !== 'undefined' && !isNaN(properties.width)) {
          mraid.expandProperties.width = properties.width;
      }

      if (properties && properties.height != null && typeof properties.height !== 'undefined' && !isNaN(properties.height)) {
          mraid.expandProperties.height = properties.height;
      }
  };



  mraid.useCustomClose = function (useCustomClose) {
      if (useCustomClose == true || useCustomClose == false) {
          mraid.expandProperties.useCustomClose = useCustomClose;
      }
  };


  mraid.fireEvent = function (event, args) {
     
      var handlers = mraid.eventListeners[event];
      if (handlers == null) { 
          return;
      }

      
      for (var handler = 0; handler < handlers.length; handler++) {
          if (event == 'ready') {
              handlers[handler]();
          } else if (event == 'error') {
              handlers[handler](args[0], args[1]);
          } else if (event == 'stateChange') {
              handlers[handler](args);
          } else if (event == 'viewableChange') {
              handlers[handler](args);
          } else if (event == 'sizeChange') {
              handlers[handler](args[0], args[1]);
          }

          
      }
  };

  
  mraid.createCalendarEvent = function (parameters) {
      callContainer('createCalendarEvent', JSON.stringify(parameters));
  };

 
  mraid.storePicture = function (url) {
      callContainer('storePicture', url);
  };

  mraid.onError = function (message, action) {
      
      mraid.fireEvent("error", [message, action]);
  };

 
  mraid.onReady = function () {
     
      mraid.onStateChange("default");
      mraid.fireEvent("ready");
  };

  
  mraid.onSizeChange = function (width, height) {
      
      mraid.fireEvent("sizeChange", [width, height]);
  };

  
  mraid.onStateChange = function (state) {
      
      mraid.state = state;
      mraid.fireEvent("stateChange", mraid.getState());
  };

 
  mraid.onViewableChange = function (isViewable) {
      
      mraid.viewable = isViewable;
      mraid.fireEvent("viewableChange", mraid.isViewable());
  };
}());