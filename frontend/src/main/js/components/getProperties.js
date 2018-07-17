export const getBackEndUrl=()=>{
    if(process.env.NODE_ENV === 'production')
        return 'https://walletforteam.herokuapp.com/';
    else
        return 'http://localhost:8080/';
}

export const dialogStyles = {
  content : {
    top                   : '50%',
    left                  : '50%',
    right                 : 'auto',
    bottom                : 'auto',
    marginRight           : '-50%',
    transform             : 'translate(-50%, -50%)',
    borderColor           : '#2185D0',
    borderWidth           : '2px'
  }
};