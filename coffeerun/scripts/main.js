(function (window) {
  'use district';
  var FORM_SELECTOR = '[data-coffee-order="form"]';
  var CHECKLIST_SELECTOR = '[data-coffee-order="checklist"]';
  var SERVER_URL = 'http://coffeerun-v2-rest-api.herokuapp.com/api/coffeeorders';
  var App = window.App;
  var Truck = App.Truck;
  var DataStore = App.DataStore;
  var RemoteDataStore = App.RemoteDataStore;
  var FormHandler = App.FormHandler;
  var CheckList = App.CheckList;
  var Validation = App.Validation;


  window.myTruck = myTruck;

  var checkList = new CheckList(CHECKLIST_SELECTOR);
  var remoteDS = new RemoteDataStore(SERVER_URL);
  var formHandler = new FormHandler(FORM_SELECTOR);
  var myTruck = new Truck('ncc-1701', myTruck);

  checkList.addClickHandler(myTruck.deliverOrder.bind(myTruck));
  formHandler.addSubmitHandler(function (data) {
    return myTruck.createOrder.call(myTruck, data)
    .then(function () {
      checkList.addRow.call(checkList, data);
    },function () {
      myTruck.db = new DataStore();
    });
  });

  formHandler.addInputHandler(Validation.isCompanyEmail);
  myTruck.printOrders(checkList.addRow.bind(checkList));
})(window);
