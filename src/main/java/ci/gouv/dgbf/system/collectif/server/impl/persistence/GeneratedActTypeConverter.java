package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import ci.gouv.dgbf.system.collectif.server.api.persistence.GeneratedAct;

@Converter(autoApply = true)
public class GeneratedActTypeConverter implements AttributeConverter<GeneratedAct.Type, String> {
 
    @Override
    public String convertToDatabaseColumn(GeneratedAct.Type type) {
    	return type.getValue();
    }
 
    @Override
    public GeneratedAct.Type convertToEntityAttribute(String value) {
    	return GeneratedAct.Type.getFromValue(value);
    }
 
}