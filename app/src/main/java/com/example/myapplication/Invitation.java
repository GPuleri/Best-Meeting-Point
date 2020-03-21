
package com.examples.bestmeetingpoint.data;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;
import com.backendless.geo.GeoPoint;

import java.util.List;
import java.util.Date;

public class Invitation
{
  private String ownerId;
  private Date updated;
  private String id_invitation;
  private String objectId;
  private Date created;
  public String getOwnerId()
  {
    return ownerId;
  }

  public Date getUpdated()
  {
    return updated;
  }

  public String getId_invitation()
  {
    return id_invitation;
  }

  public void setId_invitation( String id_invitation )
  {
    this.id_invitation = id_invitation;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public Date getCreated()
  {
    return created;
  }

                                                    
  public Invitation save()
  {
    return Backendless.Data.of( Invitation.class ).save( this );
  }

  public void saveAsync( AsyncCallback<Invitation> callback )
  {
    Backendless.Data.of( Invitation.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( Invitation.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( Invitation.class ).remove( this, callback );
  }

  public static Invitation findById( String id )
  {
    return Backendless.Data.of( Invitation.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<Invitation> callback )
  {
    Backendless.Data.of( Invitation.class ).findById( id, callback );
  }

  public static Invitation findFirst()
  {
    return Backendless.Data.of( Invitation.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<Invitation> callback )
  {
    Backendless.Data.of( Invitation.class ).findFirst( callback );
  }

  public static Invitation findLast()
  {
    return Backendless.Data.of( Invitation.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<Invitation> callback )
  {
    Backendless.Data.of( Invitation.class ).findLast( callback );
  }

  public static List<Invitation> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( Invitation.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<Invitation>> callback )
  {
    Backendless.Data.of( Invitation.class ).find( queryBuilder, callback );
  }
}