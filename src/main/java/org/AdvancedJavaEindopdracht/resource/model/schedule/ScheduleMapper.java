package org.AdvancedJavaEindopdracht.resource.model.schedule;

import org.AdvancedJavaEindopdracht.resource.model.event.Event;
import org.AdvancedJavaEindopdracht.resource.model.event.EventDto;
import org.AdvancedJavaEindopdracht.util.EntityMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduleMapper implements EntityMapper<Schedule, ScheduleDto> {
    @Override
    public ScheduleDto mapFromEntity(Schedule schedule) {
        return new ScheduleDto(
                schedule.getId(),
                schedule.getUsers(),
                schedule.getTitle(),
                schedule.getDescription(),
                schedule.getStartDateTime(),
                schedule.getEndDateTime()
        );
    }

    @Override
    public Schedule mapToEntity(ScheduleDto scheduleDto) {
        return new Schedule(
                scheduleDto.getId(),
                scheduleDto.getUsers(),
                scheduleDto.getTitle(),
                scheduleDto.getDescription(),
                scheduleDto.getStartDateTime(),
                scheduleDto.getEndDateTime()
        );
    }

    public List<ScheduleDto> mapFromEntityList(List<Schedule> entities) {
        List<ScheduleDto> ScheduleDtoList = new ArrayList<>();
        for (Schedule entity : entities) {
            ScheduleDtoList.add(mapFromEntity(entity));
        }

        return ScheduleDtoList;
    }
}
