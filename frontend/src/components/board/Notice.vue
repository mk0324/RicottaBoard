<template>
  <div>
    <v-responsive>
      <v-btn
        class="notice-button justify-center ma-3"
        fab
        :color="gradColor()"
        @click="showNotice()"
        @mouseover="isTopNoticeToggle=true"
        @mouseout="isTopNoticeToggle=false"
      >
        <img
            style="width: 36px; height:36px;"
            src="../../assets/img/noticeIconW.png"
          />
      </v-btn>
      <transition name="fade">
        <v-responsive
          class="hover notice-hover"
          v-if="isTopNoticeToggle"
          >
          <strong>공지</strong> | {{ topNotice }}
        </v-responsive>
      </transition>
    </v-responsive>

    <div v-if="isNotice" class="notice-modal">
      <div class="d-flex justify-content-between mb-4">
        <h1>게시판</h1>
        <v-icon @click="isNotice = false" large class="back-button">mdi-close</v-icon>
      </div>
      <div @click="post()" class="btn btn-success mb-3" tag="button">
        게시글 작성하기
      </div>
      <table
        class="table table-striped table-hover community"
        style="width: 100%"
      >
        <thead>
          <tr class="row m-0" style="width: 100%">
            <th class="col-2" scope="col">#</th>
            <th class="col-7" scope="col">제목</th>
            <th class="col-3" scope="col">작성자</th>
          </tr>
        </thead>
        <tbody>
          <tr
            class="row m-0"
            v-for="(article, idx) in articleList"
            :key="article.id"
            style="cursor: pointer; width: 100%"
          >
            <th class="col-2" @click="goArticleDetail(article)">
              {{ idx + 1 }}
            </th>
            <td class="col-7" @click="goArticleDetail(article)">
              <div>{{ article.title }}</div>
            </td>
            <td class="col-3">{{ article.writer }}</td>
          </tr>
        </tbody>
      </table>
      <nav aria-label="Page navigation example">
        <!-- <ul class="pagination d-inline-block" style="">
          <li class="page-item d-inline"><div class="btn btn-outline-danger" @click="goPrevious">이전</div></li>
          <li class="page-item d-inline" v-for="(pageNum, idx) in totalPage" :key="idx"><div class="btn btn-outline-danger" @click="goPage(idx)" :class="{active: checkIndex===(pageNum)}">{{ pageNum }}</div></li>
          <li class="page-item d-inline"><div class="btn btn-outline-danger" @click="goNext">다음</div></li>
        </ul> -->
      </nav>
    </div>

    <div v-if="isNotice & isPost" class="notice-modal">
      <div class="d-flex justify-content-between mb-4">
        <h1>글쓰기</h1>
        <v-icon @click="goMain()" large class="back-button">mdi-arrow-left</v-icon>
      </div>
      <div class="mb-4">자유로운 의견을 남겨주세요</div>
      <div class="text-left my-form">
        <div class="form-row">
          <label for="title">제목</label>
          <input
            v-model="articleInfo.title"
            type="text"
            class="form-control"
            id="title"
            aria-describedby="titleHelp"
          />
          <span v-if="error.title">{{ error.title }}</span>
        </div>
        <div class="form-row mt-3">
          <label for="content">내용</label>
          <textarea
            v-model="articleInfo.content"
            class="form-control"
            id="content"
            aria-describedby="contentHelp"
          ></textarea>
          <span v-if="error.content">{{ error.content }}</span>
        </div>
        <div class="form-row">
          <div class="col-4"></div>
          <button class="btn btn-success mt-5 col-4" @click="createArticle">
            작성
          </button>
          <div class="col-4"></div>
        </div>
      </div>
    </div>

    <div v-if="isNotice & isDetail" class="notice-modal">
      <div class="d-flex justify-content-between mb-4">
        <h1>게시글 상세보기</h1>
        <v-icon @click="goMain()" large class="back-button">mdi-arrow-left</v-icon>
      </div><div class="community">
        <div class="article text-left">
          
          <h3>{{ article.title }}</h3>
   
            <div class="d-flex">
              <div class="pr-5"><v-icon >mdi-account</v-icon> {{ article.writer }}</div>
              <v-icon>mdi-calendar</v-icon> {{ article.createdAt.substr(0,10) }}  {{ article.createdAt.substr(11, 8)}}<br>
            </div><hr>
          <div class="article-content">
            <p> {{ article.content }}</p>
          </div><hr>
          <div class="article-process">
            <!-- <button @click="goUpdateArticle(article)" class="btn btn-info mr-2">수정</button> -->
            <button @click="checkDelete()" class="btn btn-danger">삭제</button>
          </div>
        </div>
        <br>
        <!-- <button @click="backButton" class="btn btn-dark"> 뒤로가기</button>
        <br><hr><br>
        <Comment
          :id="this.id"
          :key="$route.fullPath"
          :userName="userName"
        /> -->
      </div>
    </div>

  </div>
</template>

<script>
import * as boardApi from "../../api/board.js";

export default {
  computed: {
    topNotice() {
      if (this.articleList.length === 0) {
        return '공지가 없습니다'
      }
      return this.articleList[0].title
    }
  },
  data() {
    return {
      articleList: [],
      isNotice: false,
      isPost: false,
      isDetail: false,
      isTopNoticeToggle: false,
      channelId: "",
      articleInfo: {
        title: "",
        content: "",
      },
      error: {
        title: "",
        content: "",
      },
      article: {},
    };
  },
  mounted() {
    this.channelId = this.$route.params.channelId
    //console.log("NOTICE 생성");
    this.fetchNotice();
  },
  methods: {
    fetchNotice() {
      boardApi.getAllNotice(this.channelId, false, this.$store.getters.accessToken,
        (response) => {
          this.articleList = response.data;
        },
        (err) => {
          //console.log("NOTICE 데이터 전송실패", err);
        }
      );
    },
    showNotice() {
      this.isNotice = !this.isNotice;
      this.isPost = false;
      this.isDetail = false;
      if(!this.isNotice) this.fetchNotice();
    },
    checkException() {
      let is_ok = true;
      if (this.articleInfo["title"] === "") {
        this.error["title"] = "제목은 빈 칸 일 수 없습니다.";
        is_ok = false;
      } else if (this.articleInfo["content"] === "") {
        this.error["content"] = "내용은 빈 칸 일 수 없습니다.";
        is_ok = false;
      }
      return is_ok;
    },
    createArticle() {
      if (this.checkException()) {
        this.articleInfo.channelId = this.channelId
        this.articleInfo.writer = this.$store.state.userData.nickname
        boardApi.createNotice(this.channelId, this.articleInfo, false, this.$store.getters.accessToken,
          (response) => {
            this.isPost = false;
            this.isDetail = true;
            this.article = response.data;
          },
          (err) => {
            //console.log("notice 생성 err", err);
          }
        );
      }
    },
    goArticleDetail(article) {
      this.isDetail = true;
      this.article = article;
    },
    checkDelete() {
      boardApi.deleteNotice(this.channelId, this.article.id, false, this.$store.getters.accessToken,
      (response) => {
        this.fetchNotice();
        this.isDetail = false;
      }),
      (err) => {
        //console.log(err);
      }
    },
    post() {
      this.isPost = !this.isPost;
      this.articleInfo.title = '';
      this.articleInfo.content = ''; 
    },
    goMain() {
      this.isDetail = false;
      this.isPost = false;
      this.fetchNotice();
    },
    gradColor() {
      if(this.isNotice) {
        return "#08543A"
      }
      return "#0d875c"
    }
  },
};
</script>

<style scoped>
.notice-button {
  position: fixed;
  z-index: 3;
  bottom: 80px;
  left: 12px;
  width: 50px;
  height: 50px;
}

.notice-modal {
  width: 80%;
  min-width: 350px;
  padding: 32px;
  margin-left: 10vw;
  margin-right: 30vw;
  margin-top: 5vh;
  margin-bottom: 30vh;
  background: #ffffff;
  border-radius: 4px;
  position: absolute;
  display: inline-block;
  max-width: 700px;
  contain: content;
  will-change: transform;
  box-shadow: 0px 5px 5px -3px rgba(0, 0, 0, 0.2),
    0px 8px 10px 1px rgba(0, 0, 0, 0.14), 0px 3px 14px 2px rgba(0, 0, 0, 0.12);
  border-radius: 4px;
  z-index: 10000;
}

span {
  color: rgb(209, 45, 45);
}

i {
  cursor: pointer;
}
.article-content {
  min-height: 120px;
}

.notice-hover{
  bottom: 98px;
}

.fade-enter-active,
.fade-leave-active{
    transition: opacity .4s
}
.fade-enter,
.fade-leave-to{
    opacity: 0
}
</style>

