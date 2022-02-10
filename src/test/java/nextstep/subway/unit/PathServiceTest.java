package nextstep.subway.unit;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class PathServiceTest {
    StationRepository stationRepository = mock(StationRepository.class);
    LineRepository lineRepository = mock(LineRepository.class);

    PathService pathService = new PathService(stationRepository, lineRepository);

    PathFinder pathFinder;
    Station 강남역;
    Station 양재역;
    Station 남부터미널역;
    List<Line> lines = new ArrayList<>();
    int totalDistance = 20;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");

        Line 이호선 = new Line("2호선", "green");
        Line 삼호선 = new Line("3호선", "orange");
        Line 신분당선 = new Line("신분당선", "red");

        Section 강남_양재 = new Section(이호선, 강남역, 양재역, 10);
        Section 강남_남부터미널 = new Section(삼호선, 강남역, 남부터미널역, 20);
        Section 양재_남부터미널 = new Section(신분당선, 양재역, 남부터미널역, 30);

        이호선.addSection(강남_양재);
        삼호선.addSection(강남_남부터미널);
        신분당선.addSection(양재_남부터미널);

        lines.add(이호선);
        lines.add(삼호선);
        lines.add(신분당선);

        pathFinder = new PathFinder(lines);
    }


    @Test
    @DisplayName("출발역과 도착역으로 경로의 최단 거리 검색")
    void searchShortestPaths() {
        // given
        given(lineRepository.findAll()).willReturn(lines);

        // when
        PathResponse response = pathService.getShortestPathResponse(pathFinder, 강남역, 남부터미널역);

        // then
        List<String> names = response.getStations().stream().map(StationResponse::getName).collect(Collectors.toList());
        assertThat(names).contains(강남역.getName(), 남부터미널역.getName());
        assertThat(response.getDistance()).isEqualTo(totalDistance);
    }
}