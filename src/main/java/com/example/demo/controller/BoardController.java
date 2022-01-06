package com.example.demo.controller;

import com.example.demo.dto.BoardDto;
import com.example.demo.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class BoardController {

    private BoardService boardService;

    public BoardController(BoardService boardService)
    {
        this.boardService = boardService;
    }

    @GetMapping("/")
    public String list(Model model)
    {
        List<BoardDto> boardDtoList = boardService.getBoardList();
        model.addAttribute("boardList", boardDtoList);
        return "board/list.html";
    }

    @GetMapping("/post")
    public String write()
    {
        return "board/write.html";
    }

    @PostMapping("/post")
    public String write(BoardDto boardDto)
    {
        boardService.savePost(boardDto);
        return "redirect:/";
    }

    @PostMapping("/post/{no}")
    public String detail(@PathVariable("no") Long id, Model model)
    {
        BoardDto boardDto = boardService.getPost(id);
        model.addAttribute("boardDto",boardDto);
        return "board/detail.html";
    }

}
