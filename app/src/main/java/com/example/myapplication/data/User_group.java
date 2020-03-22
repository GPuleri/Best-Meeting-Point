
package com.example.myapplication.data;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;

import java.util.List;
import java.util.Date;

public class User_group
{
  private Date updated;
  private Date created;
  private String type;
  private String objectId;
  private String id_user_group;
  private String ownerId;
  public Date getUpdated()
  {
    return updated;
  }

  public Date getCreated()
  {
    return created;
  }

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public String getId_user_group()
  {
    return id_user_group;
  }

  public void setId_user_group( String id_user_group )
  {
    this.id_user_group = id_user_group;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

                                                    
  public User_group save()
  {
    return Backendless.Data.of( User_group.class ).save( this );
  }

  public void saveAsync( AsyncCallback<User_group> callback )
  {
    Backendless.Data.of( User_group.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( User_group.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( User_group.class ).remove( this, callback );
  }

  public static User_group findById( String id )
  {
    return Backendless.Data.of( User_group.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<User_group> callback )
  {
    Backendless.Data.of( User_group.class ).findById( id, callback );
  }

  public static User_group findFirst()
  {
    return Backendless.Data.of( User_group.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<User_group> callback )
  {
    Backendless.Data.of( User_group.class ).findFirst( callback );
  }

  public static User_group findLast()
  {
    return Backendless.Data.of( User_group.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<User_group> callback )
  {
    Backendless.Data.of( User_group.class ).findLast( callback );
  }

  public static List<User_group> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( User_group.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<User_group>> callback )
  {
    Backendless.Data.of( User_group.class ).find( queryBuilder, callback );
  }
}