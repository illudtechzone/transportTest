package com.illud.transport.service.mapper;

import com.illud.transport.domain.*;
import com.illud.transport.service.dto.ReplyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Reply and its DTO ReplyDTO.
 */
@Mapper(componentModel = "spring", uses = {ReviewMapper.class})
public interface ReplyMapper extends EntityMapper<ReplyDTO, Reply> {

    @Mapping(source = "review.id", target = "reviewId")
    ReplyDTO toDto(Reply reply);

    @Mapping(source = "reviewId", target = "review")
    Reply toEntity(ReplyDTO replyDTO);

    default Reply fromId(Long id) {
        if (id == null) {
            return null;
        }
        Reply reply = new Reply();
        reply.setId(id);
        return reply;
    }
}
