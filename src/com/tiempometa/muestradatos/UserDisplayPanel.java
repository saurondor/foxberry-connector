/**
 * 
 */
package com.tiempometa.muestradatos;

import com.tiempometa.timing.models.Categories;
import com.tiempometa.timing.models.ParticipantRegistration;
import com.tiempometa.timing.models.Participants;
import com.tiempometa.timing.models.Registration;
import com.tiempometa.timing.models.Rfid;

/**
 * @author Gerardo Tasistro
 *
 */
public interface UserDisplayPanel {
	
	void setParticipant(Participants participant, Registration registration, Rfid rfid, Categories category);
	ParticipantRegistration getParticipant();

}
