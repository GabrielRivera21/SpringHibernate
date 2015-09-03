package com.deeplogics.cloud.json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * This class was created in order to parse the Date Object that came from
 * Spring's JSON Object into the Simple DateTimeFormat.
 *  
 * @author Gabriel
 *
 */
public class DateSerializer extends JsonSerializer<Date>{

	@Override
	public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) 
			throws IOException, JsonProcessingException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		String formattedDate = formatter.format(value);
		jgen.writeString(formattedDate);
	}

}
