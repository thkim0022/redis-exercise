package com.thkim0022.redistemplate.service;

import com.thkim0022.redistemplate.domain.PersonDto;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisTemplateStringService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * String - Set
     * @param requestDto
     */
    public void setPerson(PersonDto requestDto) {
        log.info("{}.{}", getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName());
        redisTemplate.opsForValue().set(requestDto.getKey(), requestDto);
    }

    /**
     * String - Get
     * @param personId
     * @return
     */
    public PersonDto getPerson(String personId) {
        log.info("{}.{}", getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName());
        return PersonDto.of(redisTemplate.opsForValue().get(PersonDto.keyOf(personId)));
    }

    /**
     * String - Multi Set
     * @param requestDtos
     */
    public void setPersonList(List<PersonDto> requestDtos) {
        log.info("{}.{}", getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, PersonDto> requestDtoMap = requestDtos.stream()
            .collect(Collectors.toMap(
                PersonDto::getKey,
                p2 -> p2
            ));
        redisTemplate.opsForValue().multiSet(requestDtoMap);
    }

    /**
     * String - Multi Get
     * @param personIds
     * @return
     */
    public List<PersonDto> getPersonList(List<String> personIds) {
        log.info("{}.{}", getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName());
        return redisTemplate.opsForValue()
            .multiGet(personIds.stream()
                .map(PersonDto::keyOf)
                .collect(Collectors.toList())
            )
            .stream()
            .map(PersonDto::of)
            .collect(Collectors.toList());
    }
}
