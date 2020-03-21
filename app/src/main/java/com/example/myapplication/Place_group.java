
package com.examples.bestmeetingpoint.data;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;
import com.backendless.geo.GeoPoint;

import java.util.List;
import java.util.Date;

public class Place_group
{
  private Date updated;
  private String ownerId;
  private String objectId;
  private String Id_place_group;
  private Date created;
  public Date getUpdated()
  {
    return updated;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public String getId_place_group()
  {
    return Id_place_group;
  }

  public void setId_place_group( String Id_place_group )
  {
    this.Id_place_group = Id_place_group;
  }

  public Date getCreated()
  {
    return created;
  }

                                                    
  public Place_group save()
  {
    return Backendless.Data.of( Place_group.class ).save( this );
  }

  public void saveAsync( AsyncCallback<Place_group> callback )
  {
    Backendless.Data.of( Place_group.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( Place_group.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( Place_group.class ).remove( this, callback );
  }

  public static Place_group findById( String id )
  {
    return Backendless.Data.of( Place_group.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<Place_group> callback )
  {
    Backendless.Data.of( Place_group.class ).findById( id, callback );
  }

  public static Place_group findFirst()
  {
    return Backendless.Data.of( Place_group.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<Place_group> callback )
  {
    Backendless.Data.of( Place_group.class ).findFirst( callback );
  }

  public static Place_group findLast()
  {
    return Backendless.Data.of( Place_group.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<Place_group> callback )
  {
    Backendless.Data.of( Place_group.class ).findLast( callback );
  }

  public static List<Place_group> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( Place_group.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<Place_group>> callback )
  {
    Backendless.Data.of( Place_group.class ).find( queryBuilder, callback );
  }
}