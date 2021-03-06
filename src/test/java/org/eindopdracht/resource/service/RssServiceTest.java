package org.eindopdracht.resource.service;

import org.eindopdracht.resource.dto.RssFeedDTO;
import org.eindopdracht.resource.exception.general.DataNotFoundException;
import org.eindopdracht.resource.exception.general.NoContentException;
import org.eindopdracht.resource.model.RssFeed;
import org.eindopdracht.resource.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringJUnitWebConfig(classes = org.eindopdracht.configuration.DatabaseConfigTest.class)
@Transactional
class RssServiceTest {
    @Autowired
    private RssFeedService service;

    @Test
    @Transactional
    void getRssFeeds() {
        List<RssFeedDTO> list = service.getRssFeeds();

        assertEquals(2, list.size());
    }

    @Test
    @Transactional
    void getRssFeed() {
        RssFeedDTO rss = service.getRssFeed(1);

        assertEquals("Madlyaza", rss.getUser().getName());
        assertThrows(DataNotFoundException.class, () -> {
            service.getRssFeed(23321);
        });
    }

    @Test
    @Transactional
    void createRss() throws ParseException
    {
        RssFeedDTO feedDTO = new RssFeedDTO();
        feedDTO.setLink("test");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date startDateTime = sdf.parse("10-01-2022 15:40:10");
        Date endDateTime = sdf.parse("10-01-2022 15:50:10");
        feedDTO.setStartDateTime(startDateTime);
        feedDTO.setEndDateTime(endDateTime);

        User user = new User();
        user.setName("test22");
        user.setApproved(true);
        user.setEmail("true");
        user.setPassword("true");
        user.setProfileImagePath("true");

        feedDTO.setUser(user);

        RssFeedDTO persistedFeedDTO = service.create(feedDTO);

        assertEquals("test22", persistedFeedDTO.getUser().getName());
    }

    @Test
    @Transactional
    void deleteRss() {
        service.delete(1);
        assertThrows(NoContentException.class, () -> {
            service.delete(2332);
        });
    }

    @Test
    @Transactional
    void updateRss()
    {
        RssFeedDTO feedDTO = new RssFeedDTO();
        feedDTO.setLink("test");
        feedDTO.setEndDateTime(new Date(01, 01, 2022, 00, 00, 00));
        feedDTO.setStartDateTime(new Date(01, 01, 2022, 00, 00, 00));

        User user = new User();
        user.setName("test33");
        user.setApproved(true);
        user.setEmail("true");
        user.setPassword("true");
        user.setProfileImagePath("true");
        feedDTO.setUser(user);
        RssFeedDTO persistedFeedDTO = service.update(feedDTO, 1);

        assertEquals("test33", persistedFeedDTO.getUser().getName());
    }
}