package com.example.demo.service;

import com.example.demo.domain.entity.Board;
import com.example.demo.domain.repository.BoardRepository;
import com.example.demo.dto.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    private BoardRepository boardRepository;
    private static final int BLOCK_PAGE_NUM_COUNT = 5;
    private static final int PAGE_POST_COUNT = 4;

    public BoardService(BoardRepository boardRepository)
    {
        this.boardRepository = boardRepository;
    }

    @Transactional
    public Long savePost(BoardDto boardDto)
    {
        return boardRepository.save(boardDto.toEntity()).getId();
    }

    @Transactional
    public List<BoardDto> getBoardList(Integer pageNum)
    {
        Page<Board> page = boardRepository
                .findAll(PageRequest.of(pageNum-1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC, "createDate")));
        List<Board> boards = page.getContent();
        List<BoardDto> boardDtoList = new ArrayList<>();

        for(Board board : boards)
        {
            boardDtoList.add(this.convertEntityToDto(board));
        }

//        List<Board> boards = boardRepository.findAll();
//        List<BoardDto> boardDtoList = new ArrayList<>();
//
//        for(Board board: boards)
//        {
//            BoardDto boardDto = BoardDto.builder()
//                    .id(board.getId())
//                    .title(board.getTitle())
//                    .content(board.getContent())
//                    .writer(board.getWriter())
//                    .createDate(board.getCreateDate())
//                    .build();
//            boardDtoList.add(boardDto);
//        }
        return boardDtoList;
    }

    @Transactional
    public BoardDto getPost(Long id)
    {
        Optional<Board> boardWrapper = boardRepository.findById(id);
        Board board = boardWrapper.get();

        BoardDto boardDto = BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .createDate(board.getCreateDate())
                .build();
        return boardDto;
    }

    @Transactional
    public void deletePost(Long id)
    {
        boardRepository.deleteById(id);
    }

    @Transactional
    public List<BoardDto> searchPosts(String keyword)
    {
        List<Board> boards = boardRepository.findByTitleContaining(keyword);
        List<BoardDto> boardDtoList = new ArrayList<>();

        if(boards.isEmpty())
        {
            return boardDtoList;
        }

        for(Board board : boards)
        {
            boardDtoList.add(this.convertEntityToDto(board));
        }
        return boardDtoList;
    }

    private BoardDto convertEntityToDto(Board board)
    {
        return BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .createDate(board.getCreateDate())
                .build();
    }

    public Integer[] getPageList(Integer curPageNum)
    {
        Integer[] pageList = new Integer[BLOCK_PAGE_NUM_COUNT];
        Double postsTotalCount = Double.valueOf(this.getBoardCount());

        Integer totalLastPageNum = (int)(Math.ceil((postsTotalCount / PAGE_POST_COUNT)));
        Integer blockLastPageNum = (totalLastPageNum > curPageNum + BLOCK_PAGE_NUM_COUNT)
                ? curPageNum + BLOCK_PAGE_NUM_COUNT
                : totalLastPageNum;

        curPageNum = (curPageNum<=3) ? 1 : curPageNum-2;

        for(int val=curPageNum, i=0; val <= blockLastPageNum; val++, i++)
        {
            pageList[i] = val;
        }
        return pageList;
    }

    @Transactional
    public Long getBoardCount()
    {
        return boardRepository.count();
    }
}
