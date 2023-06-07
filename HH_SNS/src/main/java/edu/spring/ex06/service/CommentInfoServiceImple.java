package edu.spring.ex06.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.spring.ex06.domain.CommentInfoVO;
import edu.spring.ex06.domain.FeedVO;
import edu.spring.ex06.domain.LikeInfoVO;
import edu.spring.ex06.domain.ReplyVO;
import edu.spring.ex06.persistence.CommentInfoDAO;
import edu.spring.ex06.persistence.FeedDAO;
import edu.spring.ex06.persistence.LikeInfoDAO;
import edu.spring.ex06.persistence.ReplyDAO;

@Service
public class CommentInfoServiceImple implements CommentInfoService{
	private static final Logger logger =
			LoggerFactory.getLogger(CommentInfoServiceImple.class);
	
	@Autowired
	private FeedDAO feeddao;
	
	@Autowired
	private LikeInfoDAO likedao;
	
	@Autowired
	private ReplyDAO replydao;
	
	@Autowired
	private CommentInfoDAO commentdao;

	@Override
	public int create(CommentInfoVO commentvo) {
		logger.info("★ CommentInfoServiceImple 대댓글 등록 = " + commentvo.toString());
		return commentdao.insert(commentvo);
	}// end create()
	
	@Override
	public CommentInfoVO read(int commentId) {
		logger.info("★ CommentInfoServiceImple 검색 : 대댓글 번호 = " + commentId);
		return commentdao.select(commentId);
	}
	
	@Override
	public CommentInfoVO read_reply(int replyId) {
		logger.info("★ CommentInfoServiceImple 검색 : 대댓글의 댓글 번호 = " + replyId);
		return commentdao.select_reply(replyId);
	}
	
	@Override
	public List<CommentInfoVO> read_all(int replyId) {
		logger.info("★ CommentInfoServiceImple 전체 검색 : = " + replyId);
		return commentdao.select_all(replyId);
	}
	
	@Override
	public int update(int commentId, String commentContent) {
		logger.info("★ CommentInfoServiceImple 수정");
		logger.info("대댓글 번호 = " + commentId + ", 대댓글 내용 = " + commentContent);
		return commentdao.update(commentId, commentContent);
	}

	@Override
	public int delete(int commentId) {
		logger.info("★ CommentInfoServiceImple 대댓글 삭제");
		return commentdao.delete(commentId);
	}





}
