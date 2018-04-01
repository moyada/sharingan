import fetch from 'dva/fetch';
import querystring from 'querystring';


// let host;
// {
//   const local = this.location.host
//   const path = this.location.pathname
//   const index = path.indexOf('/' , 1)
//   if(-1 === index) {
//     host = local
//   }
//   else {
//     host = local + path.substring(0, index)
//   }
//   console.log(host)
// }

function parseJSON(response) {
  return response.json();
}

function checkStatus(response) {
  if (response.status >= 200 && response.status < 300) {
    return response;
  }
  const error = new Error(response.statusText);
  error.response = response;
  throw error;
}

/**
 * Requests a URL, returning a promise.
 *
 * @param  {string} url       The URL we want to request
 * @param  {object} [options] The options we want to pass to "fetch"
 * @return {object}           An object containing either "data" or "err"
 */
export default function request(url, payload, method) {
  if(null == method) {
    method = 'GET';
  }

  switch (method) {
    case 'POST':
      return postRequest(url, payload, method)
    case 'GET':
      return getRequest(url, payload, method)
    default:
      throw new Error('invalid request method');
  }
}



function getRequest(url, payload, method) {
  return fetch(url + `?` + querystring.encode(payload),
    {
      method: method,
      credentials: 'include'
    })
    .then(checkStatus)
    .then(parseJSON)
    .then(data => ({ data }))
    .catch(err => ({ err }));
}


function postRequest(url, payload, method) {
  return fetch(url,
    {
      method: method,
      body: querystring.encode(payload),
      headers: {
        "Content-Type": 'application/x-www-form-urlencoded'
      },
      credentials: 'include'
    })
    .then(checkStatus)
    .then(parseJSON)
    .then(data => ({ data }))
    .catch(err => ({ err }));
}
