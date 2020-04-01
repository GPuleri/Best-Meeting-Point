package com.example.myapplication.data;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;
import com.backendless.geo.GeoPoint;

import java.util.List;
import java.util.Date;

public class Place
{
    private String city;
    private String country;
    private Date created;
    private String ownerId;
    private String address;
    private String objectId;
    private Date updated;
    private String full_address;
    private String id_google_place;

    public String getId_google_place() {
        return id_google_place;
    }

    public void setId_google_place(String id_google_place) {
        this.id_google_place = id_google_place;
    }

    public String getFull_address() {
        return full_address;
    }

    public void setFull_address(String full_address) {
        this.full_address = full_address;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity( String city )
    {
        this.city = city;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry( String country )
    {
        this.country = country;
    }

    public Date getCreated()
    {
        return created;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress( String address )
    {
        this.address = address;
    }

    public String getObjectId()
    {
        return objectId;
    }

    public Date getUpdated()
    {
        return updated;
    }


    public Place save()
    {
        return Backendless.Data.of( Place.class ).save( this );
    }

    public void saveAsync( AsyncCallback<Place> callback )
    {
        Backendless.Data.of( Place.class ).save( this, callback );
    }

    public Long remove()
    {
        return Backendless.Data.of( Place.class ).remove( this );
    }

    public void removeAsync( AsyncCallback<Long> callback )
    {
        Backendless.Data.of( Place.class ).remove( this, callback );
    }

    public static Place findById( String id )
    {
        return Backendless.Data.of( Place.class ).findById( id );
    }

    public static void findByIdAsync( String id, AsyncCallback<Place> callback )
    {
        Backendless.Data.of( Place.class ).findById( id, callback );
    }

    public static Place findFirst()
    {
        return Backendless.Data.of( Place.class ).findFirst();
    }

    public static void findFirstAsync( AsyncCallback<Place> callback )
    {
        Backendless.Data.of( Place.class ).findFirst( callback );
    }

    public static Place findLast()
    {
        return Backendless.Data.of( Place.class ).findLast();
    }

    public static void findLastAsync( AsyncCallback<Place> callback )
    {
        Backendless.Data.of( Place.class ).findLast( callback );
    }

    public static List<Place> find( DataQueryBuilder queryBuilder )
    {
        return Backendless.Data.of( Place.class ).find( queryBuilder );
    }

    public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<Place>> callback )
    {
        Backendless.Data.of( Place.class ).find( queryBuilder, callback );
    }
}

