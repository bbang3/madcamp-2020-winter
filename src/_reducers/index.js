import { combineReducers } from 'redux';
import user from './user_reducer';
//import comment from './comment_reducer'; => 만약에 comment 기능을 만든다고 하면 합쳐질 reducer 들임...

const rootReducer = combineReducers({
    user
})

export default rootReducer;
//state가 변한것들 알려주는 것이 reducer
//각각의 state마다 reducer가 나뉘어져 있다. ex) user 등등

//rootReducer에서 combineReducer를 이용해서 하나로 합쳐주는 역할을 한다.

//지금은 user_reducer만 추가되어 있지만 나중에는 더 추가할 예정임...?)
