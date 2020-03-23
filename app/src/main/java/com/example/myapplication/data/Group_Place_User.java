
package com.example.myapplication.data;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;
import com.backendless.geo.GeoPoint;

import java.util.List;
import java.util.Date;

public class Group_Place_User
{
  private Date updated;
  private Date created;
  private String ownerId;
  private String objectId;
  public Date getUpdated()
  {
    return updated;
  }

  public Date getCreated()
  {
    return created;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

  public String getObjectId()
  {
    return objectId;
  }

                                                    
  public Group_Place_User save()
  {
    return Backendless.Data.of( Group_Place_User.class ).save( this );
  }

  public void saveAsync( AsyncCallback<Group_Place_User> callback )
  {
    Backendless.Data.of( Group_Place_User.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( Group_Place_User.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( Group_Place_User.class ).remove( this, callback );
  }

  public static Group_Place_User findById( String id )
  {
    return Backendless.Data.of( Group_Place_User.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<Group_Place_User> callback )
  {
    Backendless.Data.of( Group_Place_User.class ).findById( id, callback );
  }

  public static Group_Place_User findFirst()
  {
    return Backendless.Data.of( Group_Place_User.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<Group_Place_User> callback )
  {
    Backendless.Data.of( Group_Place_User.class ).findFirst( callback );
  }

  public static Group_Place_User findLast()
  {
    return Backendless.Data.of( Group_Place_User.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<Group_Place_User> callback )
  {
    Backendless.Data.of( Group_Place_User.class ).findLast( callback );
  }

  public static List<Group_Place_User> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( Group_Place_User.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<Group_Place_User>> callback )
  {
    Backendless.Data.of( Group_Place_User.class ).find( queryBuilder, callback );
  }
}