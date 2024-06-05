package com.kh.totalEx.repository;


import com.kh.totalEx.entity.Board;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;

    @Test
    @DisplayName("게시글 저장 테스트")
    public void createBoardTest() {
        for(int i = 1; i <= 10; i++) {
            Board board = new Board();
            board.setTitle("Title" + i);
            board.setContent("Content" + i);
            board.setImgPath("url" + i);
            board.setRegDate(LocalDateTime.now());
            boardRepository.save(board);
        }
    }

    @Test
    @DisplayName("게시글 전체 조회 테스트")
    public void findAll() {
        this.createBoardTest();
        List<Board> boardList = boardRepository.findAll();
        for(Board board : boardList) System.out.println(board.getContent());
    }

    @Test
    @DisplayName("키워드로 게시글 조회")
    public void findByTitleContaining() {
        this.createBoardTest();
        List<Board> boardList = boardRepository.findByTitleContaining("Con");
        for (Board board : boardList) System.out.println("게시글 : " + board.toString());
    }

}