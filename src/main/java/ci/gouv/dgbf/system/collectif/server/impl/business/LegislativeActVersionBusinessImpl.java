package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cyk.utility.business.server.AbstractSpecificBusinessImpl;

import ci.gouv.dgbf.system.collectif.server.api.business.LegislativeActVersionBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;

@ApplicationScoped
public class LegislativeActVersionBusinessImpl extends AbstractSpecificBusinessImpl<LegislativeActVersion> implements LegislativeActVersionBusiness,Serializable {

	@Inject LegislativeActVersionPersistence persistence;
	
}