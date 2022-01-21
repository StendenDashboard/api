package org.eindopdracht.resource.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eindopdracht.resource.model.event.content.Content;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false,cascade = CascadeType.ALL)
    @JoinColumn(name="content_id")
    private Content content;

    private Long user_id;

    private String description;

    private Date startDateTime;

    private Date endDateTime;

    private Long duration;
}