import {
    LOGIN_USER,
    REGISTER_USER
}from '../_actions/types';

export default function foo(state = {}, action){//각각 previous state과 request임 
    switch (action.type) {
        case LOGIN_USER:
            return {...state, loginSuccess : action.payload} //nextstate를 return 해야함
            //action payload에 우리가 sever에서 받아온 정보가 담겨있음 
            case REGISTER_USER:
                return {...state, register : action.payload} //nextstate를 return 해야함
                //action payload에 우리가 sever에서 받아온 정보가 담겨있음 
    
        default:
            return state;
        
    }


}

//우리는 functional component를 이용할 것임 ==> hook 
// useState를 먼저 하고 return 하고 useEffect를 해서 우리가 하고 싶은 것들을 구현하는 것이다.
