package nextstep.subway.unit;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class LineServiceTest {
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private LineService lineService;

    @Test
    void addSection() {
        // given
        // stationRepository와 lineRepository를 활용하여 초기값 셋팅
        Station upStation = new Station("서초역");
        Station downStation = new Station("강남역");
        stationRepository.save(upStation);
        stationRepository.save(downStation);

        Line line = new Line("2호선", "green");
        Line saveLine = lineRepository.save(line);

        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), 10);

        // when
        // lineService.addSection 호출
        lineService.addSection(saveLine.getId(), sectionRequest);

        // then
        // line.getSections 메서드를 통해 검증
        assertThat(line.getSections())
                .filteredOn(s -> upStation.getName().equals(s.getUpStation().getName()) &&
                        downStation.getName().equals(s.getDownStation().getName())).hasSize(1);

    }
}
