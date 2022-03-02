<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-6.14.15-blue">
  <img alt="node" src="https://img.shields.io/badge/node-14.18.2-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
</p>

<br>

# 지하철 노선도 미션
## 🚀 1단계 - 구간 추가 기능 변경
### 변경된 스펙 - 구간 추가 제약사항 변경
**역 사이에 새로운 역을 등록할 경우**
- [x] 기존 구간의 역을 기준으로 새로운 구간을 추가
- [x] 기존 구간 A-C에 신규 구간 A-B를 추가하는 경우 A역을 기준으로 추가
- [x] 기존 구간과 신규 구간이 모두 같을 순 없음(아래 예외사항에 기재됨)
- [x] 결과로 A-B, B-C 구간이 생김
- [x] 새로운 길이를 뺀 나머지를 새롭게 추가된 역과의 길이로 설정

**노선 조회시 응답되는 역 목록 수정**
- [x] 구간이 저장되는 순서로 역 목록을 조회할 경우 순서가 다르게 조회될 수 있음
- [x] 아래의 순서대로 역 목록을 응답하는 로직을 변경해야 함
    1. 상행 종점이 상행역인 구간을 먼저 찾는다.
    2. 그 다음, 해당 구간의 하행역이 상행역인 다른 구간을 찾는다.
    3. 2번을 반복하다가 하행 종점역을 찾으면 조회를 멈춘다.
   
### 변경된 스펙 - 예외 케이스
- [x] 역 사이에 새로운 역을 등록할 경우 기존 역 사이 길이보다 크거나 같으면 등록을 할 수 없음
- [x] 상행역과 하행역이 이미 노선에 모두 등록되어 있다면 추가할 수 없음
- [x] 상행역과 하행역 둘 중 하나도 포함되어있지 않으면 추가할 수 없음

<br>

## 🚀 2단계 - 구간 제거 기능 변경
### 변경된 스펙
**구간 삭제에 대한 제약 사항 변경 구현**
- [x] 기존에는 마지막 역 삭제만 가능했는데 위치에 상관 없이 삭제가 가능하도록 수정
- [x] 종점이 제거될 경우 다음으로 오던 역이 종점이 됨
- [x] 중간역이 제거될 경우 재배치를 함
    1. 노선에 A - B - C 역이 연결되어 있을 때 B역을 제거할 경우 A - C로 재배치 됨
    2. 거리는 두 구간의 거리의 합으로 정함
<br>

## 🚀 3단계 - 경로 조회
**다익스트라 알고리즘 라이브러리 학습을 위한 학습 테스트**
```
@Test
public void getDijkstraShortestPath() {
    WeightedMultigraph<String, DefaultWeightedEdge> graph
            = new WeightedMultigraph(DefaultWeightedEdge.class);
    graph.addVertex("v1");
    graph.addVertex("v2");
    graph.addVertex("v3");
    graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
    graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
    graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

    DijkstraShortestPath dijkstraShortestPath
            = new DijkstraShortestPath(graph);
    List<String> shortestPath 
            = dijkstraShortestPath.getPath("v3", "v1").getVertexList();

    assertThat(shortestPath.size()).isEqualTo(3);
}
```

**외부 라이브러리 테스트**
- [x] 외부 라이브러리의 구현을 수정할 수 없기 때문에 단위 테스트를 하지 않음
- [x] 외부 라이브러리를 사용하는 직접 구현하는 로직을 검증해야 함
- [x] 직접 구현하는 로직 검증 시 외부 라이브러리 부분은 실제 객체를 활용

<br>

### 기능 구현
**Outside In 경우**
* 컨트롤러 레이어 구현 이후 서비스 레이어 구현 시 서비스 테스트 우선 작성 후 기능 구현
* 서비스 테스트 내부에서 도메인들간의 로직의 흐름을 검증, 이 때 사용되는 도메인은 mock 객체를 활용
* 외부 라이브러리를 활용한 로직을 검증할 때는 가급적 실제 객체를 활용
* Happy 케이스에 대한 부분만 구현( Side 케이스에 대한 구현은 다음 단계에서 진행)

**Inside Out 경우**
* 도메인 설계 후 도메인 테스트를 시작으로 기능 구현 시작
* 해당 도메인의 단위 테스트를 통해 도메인의 역할과 경계를 설계
* 도메인의 구현이 끝나면 해당 도메인과 관계를 맺는 객체에 대해 기능 구현 시작
