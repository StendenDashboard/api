package org.eindopdracht.resource.service;

import org.eindopdracht.resource.dto.ScheduleDTO;
import org.eindopdracht.resource.exception.general.BadRequestException;
import org.eindopdracht.resource.exception.general.DataNotFoundException;
import org.eindopdracht.resource.exception.general.NoContentException;
import org.eindopdracht.resource.mapper.ScheduleMapper;
import org.eindopdracht.resource.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;

    public ScheduleService(ScheduleRepository scheduleRepository, ScheduleMapper scheduleMapper) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleMapper = scheduleMapper;
    }

    /**
     * Maps Entity to DTO and returns a list of all schedules.
     *
     * @return response entity with list of all schedules
     */
    public List<ScheduleDTO> getAll() {
        return scheduleMapper.mapFromEntityList(scheduleRepository.get());
    }

    /**
     * Maps Entity to DTO and returns a single schedule.
     *
     * @param id id of the schedule to find
     * @return response entity with single schedule
     */
    public ScheduleDTO getById(int id) {
        try {
            return scheduleMapper.mapFromEntity(scheduleRepository.getById(id));
        } catch (Exception ex) {
            throw new DataNotFoundException("id: " + id);
        }
    }

    /**
     * Maps Entity to DTO and posts a single schedule.
     *
     * @param scheduleDto schedule to post
     * @return response entity with posted schedule
     */
    public ScheduleDTO persist(ScheduleDTO scheduleDto) {
        try {
            return scheduleMapper.mapFromEntity(
                    scheduleRepository.post(scheduleMapper.mapToEntity(scheduleDto))
            );
        } catch (Exception ex) {
            throw new BadRequestException();
        }
    }

    /**
     * Maps Entity to DTO and puts a single schedule.
     *
     * @param id          id of the schedule to put
     * @param scheduleDto schedule to put
     * @return response entity with put schedule
     */
    public ScheduleDTO put(int id, ScheduleDTO scheduleDto) {
        try {
            return scheduleMapper.mapFromEntity(scheduleRepository.put(id, scheduleMapper.mapToEntity(scheduleDto)));
        } catch (Exception ex) {
            throw new BadRequestException();
        }
    }

    /**
     * Maps Entity to DTO and deletes a single schedule.
     *
     * @param id id of the schedule to delete
     * @return response entity with deleted schedule
     */
    public ScheduleDTO delete(int id) {
        try {
            return scheduleMapper.mapFromEntity(scheduleRepository.delete(id));
        } catch (Exception ex) {
            throw new NoContentException("id: " + id);
        }
    }
}
