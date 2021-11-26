package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.cyk.utility.service.server.AbstractSpecificServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Section;
import ci.gouv.dgbf.system.collectif.server.api.service.SectionDto;
import ci.gouv.dgbf.system.collectif.server.api.service.SectionService;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.SectionImpl;

@Path(SectionService.PATH)
public class SectionServiceImpl extends AbstractSpecificServiceImpl<SectionDto,SectionDtoImpl,Section,SectionImpl> implements SectionService,Serializable {

	@Inject SectionDtoImplMapper mapper;
	
	public SectionServiceImpl() {
		this.serviceEntityClass = SectionDto.class;
		this.serviceEntityImplClass = SectionDtoImpl.class;
		this.persistenceEntityClass = Section.class;
		this.persistenceEntityImplClass = SectionImpl.class;
	}
}