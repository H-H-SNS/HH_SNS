package edu.spring.ex06.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.spring.ex06.domain.FeedVO;

@Repository
public class FeedDAOImple implements FeedDAO{
	private static final Logger logger = LoggerFactory.getLogger(FeedDAOImple.class);
	private static final String NAMESPACE = "edu.spring.ex06.FeedMapper";

	@Autowired
	private SqlSession sqlSession;
	
	/*
	 * int insert(String userId, FeedVO vo); // 피드 작성(회원)
	   List<FeedVO> selectAll(String userId, Date feedDate); // 전체 피드 츌력 = 전체 검색 (최신순)(비회원)
	   FeedVO select (String userId, int feedId); // 피드 상세 출력 = 아이디 기준 검색(비회원)
	   int update(String userId, FeedVO vo); // 피드 수정(회원)
	   int delete(String usserId); // 아이디 기준 피드 삭제(회원)
	   List<FeedVO> selectAllbyId (String userId); // 개인 피드 출력 = 아이디 기준(회원)
	 * */

	@Override
	public int insert(FeedVO vo) {
		logger.info("★ FeedDAOImple 피드 등록");
		return sqlSession.insert(NAMESPACE + ".insert", vo);
	}// end insert
	
	@Override
	public List<FeedVO> selectSearch(String userId, String userNickname, String feedContent) {
		logger.info("★ FeedDAOImple 피드 포함 단어 검색");
		Map<String, Object>  args = new HashMap<>();
		args.put("userId", userId);
		args.put("userNickname", userNickname);
		args.put("feedContent", feedContent);
		return sqlSession.selectOne(NAMESPACE + ".select_search", args);
	}

	@Override
	public List<FeedVO> selectAll() {
		logger.info("★ FeedDAOImple 전체검색 최신순");
		return sqlSession.selectList(NAMESPACE + ".select_all");
	}// end selectAll 전체 검색

	@Override
	public List<FeedVO> selectAllbyId(String userId) {
		logger.info("★ FeedDAOImple 개인 피드");
		return sqlSession.selectList(NAMESPACE +".select_all_by_userid", userId);
	}// end selectAllbyId 개인 피드 전체 출력
	
	@Override
	public FeedVO select(int feedId) {
		logger.info("★ FeedDAOImple 피드번호 detail");
		return sqlSession.selectOne(NAMESPACE + ".select", feedId);
	}// end select 상세 검색
	
	@Override
	public FeedVO select(int feedId, String userId) {
		logger.info("★ FeedDAOImple 피드번호, 유저아이디 detail");
		Map<String, Object>  args = new HashMap<>();
		args.put("feedId", feedId);
		args.put("userId", userId);
		return sqlSession.selectOne(NAMESPACE + ".select_by_id", args);
	}// end select_by_id 상세 검색

	
	@Override
	public FeedVO select(String userId) {
		logger.info("★ FeedDAOImple 유저아이디 detail");
		return sqlSession.selectOne(NAMESPACE + ".select_by_userid", userId);
	}// end select 상세 검색

	@Override
	public int update(int feedId, String feedContent) {
		logger.info("★ FeedDAOImple 피드 수정");
		logger.info("feedId = " + feedId + " feedContent = " + feedContent);
		Map<String, Object>  args = new HashMap<>();
		args.put("feedId", feedId);
		args.put("feedContent", feedContent);
		return sqlSession.update(NAMESPACE + ".update", args);
	}// end update 수정

	@Override
	public int delete(int feedId) {
		logger.info("★ FeedDAOImple 피드 삭제");
		return sqlSession.delete(NAMESPACE + ".delete", feedId);
	}// end delete 삭제
	
	@Override
	public int getTotalLike() {
		logger.info("★ FeedDAOImple 좋아요 수");
		return sqlSession.selectOne(NAMESPACE, ".total_like");
	}
	
	@Override
	public int getTotalComment() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int updateLikeCnt(int amount, int feedId) {
		logger.info("★ FeedDAOImple 좋아요 개수 : feedId = " + feedId);
		Map<String, Integer> args = new HashMap<>();
		args.put("amount", amount);
		args.put("feedId", feedId);
		return sqlSession.update(NAMESPACE + ".update_like_cnt", args);
	}

	@Override
	public int updateCommentCnt(int amount, int feedId) {
		// TODO Auto-generated method stub
		return 0;
	}// end updateCommentCnt 개인 댓글 출력

	


}
