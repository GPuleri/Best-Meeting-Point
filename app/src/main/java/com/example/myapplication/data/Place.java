
package com.example.myapplication.data;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;

import java.util.List;
import java.util.Date;

public class Place
{
  private String ownerId;
  private String address;
  private Date created;
  private Date updated;
  private String country;
  private String id_place;
  private String city;
  private String objectId;
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

  public Date getCreated()
  {
    return created;
  }

  public Date getUpdated()
  {
    return updated;
  }

  public String getCountry()
  {
    return country;
  }

  public void setCountry( String country )
  {
    this.country = country;
  }

  public String getId_place()
  {
    return id_place;
  }

  public void setId_place( String id_place )
  {
    this.id_place = id_place;
  }

  public String getCity()
  {
    return city;
  }

  public void setCity( String city )
  {
    this.city = city;
  }

  public String getObjectId()
  {
    return objectId;
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