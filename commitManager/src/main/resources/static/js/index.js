// const ajax = function({uri, callback}) {
//   const x = new XMLHttpRequest();
//   x.addEventListener('load', () => {
//     const data = JSON.parse(x.response);
//     callback(data);
//   })
//   x.open('GET', uri);
//   x.send();
// }

const ajax = function({uri, callback}) {
  const dummyData = {
    createdAt: "2018-08-14 08:02:13",
    commits: [{message: 'fix: bug fix'}, {message: 'feat: add some feature'}, {message: 'feat: add some feature'}, {message: 'feat: add some feature'}]
  }
  callback(dummyData);
}

const template = function(message) {
  return `<li class='commit_list_item'>${message}</li>`;
}

// @param {number} month - 0 ~ 11
const dayCalculator = function({month, date}) {
  const map = [0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
  let result = 0;

  for(let i = 0; i <= month; i++) {
    result += map[i];
  }

  return result + date-1;
}

class DateCalculator {
  constructor({nicknameElem, lastCommitTimeElem, restTimeElem, commitList, nickname, uri, ajax, dayCalculator, template}) {
    this.elNicknameElem = nicknameElem;
    this.elLastCommitTime = lastCommitTimeElem;
    this.elRestTime = restTimeElem;
    this.elCommitList = commitList;
    this.nickname = nickname;
    this.uri = uri;
    this.ajax = ajax
    this.dayCalculator = dayCalculator;
    this.template = template;
    this.restTime = null;
  }

  init() {
    this.setName(this.nickname);
    this.ajax({uri: this.uri, callback: this.set.bind(this)});
  }

  setName(nickname) {
    this.elNicknameElem.textContent = nickname;
  }

  set(ajaxData) {
    const lastCommitDate = ajaxData.createdAt;
    const commitList = ajaxData.commits;

    this.setLastCommitTime(lastCommitDate);
    this.setRestTime();
    this.renderCommitList(commitList);
  }

  setLastCommitTime(date) {
    const currentTime = new Date().getTime();
    const lastCommitTime = new Date(date).getTime();

    const restTime = new Date(currentTime - lastCommitTime);

    const restTimeMonth = restTime.getMonth();
    const restTimeDate = restTime.getDate();
    const restTimeHours = restTime.getHours();
    const restTimeMinutes = restTime.getMinutes();
    const restTimeSeconds = restTime.getSeconds();
    
    const restDays = this.dayCalculator({month: restTimeMonth, date: restTimeDate});

    const resultDate = `${restDays}일 ${restTimeHours}시간 ${restTimeMinutes}분 ${restTimeSeconds}초`;

    this.elLastCommitTime.textContent = resultDate;
  }

  setRestTime() {
    const currentTime = new Date();
    const restHours = 23 - currentTime.getHours();
    const restMinutes = 59 - currentTime.getMinutes();
    const restSeconds = 59 - currentTime.getSeconds();

    this.restTime = {hours: restHours, minutes: restMinutes, seconds: restSeconds};

    this.renderRestTime(this.restTime);
  }
  
  // @parma {Object} timedata - {hours, minutes, seconds}
  renderRestTime(timedata) {
    let hours = timedata.hours.toString();
    let minutes = timedata.minutes.toString();
    let seconds = timedata.seconds.toString();

    hours = hours.padStart(2,'0');
    minutes = minutes.padStart(2,'0');
    seconds = seconds.padStart(2,'0');

    this.elRestTime.textContent = `${hours}:${minutes}:${seconds}`
    
    setTimeout(this.setRestTime.bind(this), 1000);
  }

  renderCommitList(commits) {
    this.elCommitList.innerHTML = commits.reduce((html, commit) => html += this.template(commit.message), '');
  }
}


const dateCalculator = new DateCalculator({
  nicknameElem: document.querySelector('.nickname'),
  lastCommitTimeElem: document.querySelector('.last_commit_time'),
  restTimeElem: document.querySelector('.rest_time'),
  commitList: document.querySelector('.commit_list'),
  nickname: 'mando',
  uri: 'http://13.209.88.99/api/commit/recent',
  ajax: ajax,
  dayCalculator: dayCalculator,
  template: template
})

dateCalculator.init();