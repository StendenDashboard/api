package org.AdvancedJavaEindopdracht.resource.model.consultation;

import org.AdvancedJavaEindopdracht.resource.model.event.Event;
import org.AdvancedJavaEindopdracht.resource.model.event.EventDto;
import org.AdvancedJavaEindopdracht.util.EntityMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConsultationMapper implements EntityMapper<Consultation, ConsultationDto> {
    @Override
    public ConsultationDto mapFromEntity(Consultation consultation) {
        return new ConsultationDto(
                consultation.getId(),
                consultation.getUsers(),
                consultation.getStartDateTime(),
                consultation.getEndDateTime()
        );
    }

    @Override
    public Consultation mapToEntity(ConsultationDto consultationDto) {
        return new Consultation(
                consultationDto.getId(),
                consultationDto.getUsers(),
                consultationDto.getStartDateTime(),
                consultationDto.getEndDateTime()
        );
    }

    public List<ConsultationDto> mapFromEntityList(List<Consultation> entities) {
        List<ConsultationDto> ConsultationDtoList = new ArrayList<>();
        for (Consultation entity : entities) {
            ConsultationDtoList.add(mapFromEntity(entity));
        }

        return ConsultationDtoList;
    }
}
