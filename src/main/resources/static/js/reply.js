// async function getRepliesByBoardId(boardId) { // 기존의 get1에서 의미 있는 함수명으로 수정
//     const result = await axios.get(`/replies/list/${boardId}`);
//     return result;
// }
//
// async function getReplyList({ boardId, page, size, goLast }) { // 기존의 getList에서 명확한 이름으로 수정
//     const result = await axios.get(`/replies/list/${boardId}`, { params: { page, size } });
//
//     if (goLast) {
//         const total = result.data.total;
//         const lastPage = Math.ceil(total / size);
//
//         return getReplyList({ boardId: boardId, page: lastPage, size: size });
//     }
//
//     return result.data;
// }

async function addReply(replyObj) {
    const response = await axios.post(`/replies/`, replyObj);
    return response.data;
}

async function getReply(replyId) { // 기존의 rno를 replyId로 수정
    const response = await axios.get(`/replies/${replyId}`);
    return response.data;
}

async function modifyReply(replyObj) {
    const response = await axios.put(`/replies/${replyObj.replyId}`, replyObj); // 기존의 rno를 replyId로 수정
    return response.data;
}

async function removeReply(replyId) { // 기존의 rno를 replyId로 수정
    const response = await axios.delete(`/replies/${replyId}`);
    return response.data;
}
