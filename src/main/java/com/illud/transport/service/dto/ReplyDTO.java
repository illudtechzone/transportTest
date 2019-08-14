package com.illud.transport.service.dto;
import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Reply entity.
 */
public class ReplyDTO implements Serializable {

    private Long id;

    private String iDPcode;

    private String reply;

    private Instant repliedDate;


    private Long reviewId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getiDPcode() {
        return iDPcode;
    }

    public void setiDPcode(String iDPcode) {
        this.iDPcode = iDPcode;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public Instant getRepliedDate() {
        return repliedDate;
    }

    public void setRepliedDate(Instant repliedDate) {
        this.repliedDate = repliedDate;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReplyDTO replyDTO = (ReplyDTO) o;
        if (replyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), replyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReplyDTO{" +
            "id=" + getId() +
            ", iDPcode='" + getiDPcode() + "'" +
            ", reply='" + getReply() + "'" +
            ", repliedDate='" + getRepliedDate() + "'" +
            ", review=" + getReviewId() +
            "}";
    }
}
