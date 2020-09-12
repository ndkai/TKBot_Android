package com.example.iot_app.data;
import com.example.iot_app.data.api.IApi;
import com.example.iot_app.data.db.IDb;
import com.example.iot_app.data.pref.IPref;

/*
* The data port for all of the presenters accessing through
* */
public interface IDataManager extends IApi, IDb, IPref {

}
