const mysql = require('mysql2');
const fs = require('fs');

const conn = mysql.createConnection({

  dateStrings : "date" // MySQL datetime 형식을 그대로 문자열로 반환
});

conn.connect((err) => {
  if (err) {
    console.error('MySQL 연결 실패:', err.stack);
    return;
  }
  console.log("MySQL 연동 성공");

  // 필요한 컬럼만 선택하여 쿼리
  const query = `
    SELECT tag, location, upload_datetime, photo_id, user_id, lng, lat, likes, views
    FROM Photo
  `;
  
  conn.query(query, (err, results) => {
    if (err) {
      console.error("쿼리 실행 중 에러:", err);
      conn.end();
      throw err;
    }
    
    // 각 결과에 content: "" 필드 추가
    const modifiedResults = results.map(row => ({
      ...row,
      content: ""
    }));
    
    // 결과를 JSON 문자열로 변환하여 파일에 저장 (가독성을 위해 들여쓰기 2칸)
    fs.writeFile('./json.txt', JSON.stringify(modifiedResults, null, 2), (err) => {
      if (err) {
        console.error('파일 저장 중 에러:', err);
      } else {
        console.log("쿼리 결과가 './json.txt' 파일에 저장되었습니다.");
      }
      conn.end();
    });
  });
});
