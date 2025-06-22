import axios from 'axios'
const baseUrl = 'http://localhost:8080/'   //后端服务接口基地址

const apiAxios = async (method, url, params) => {
    let headers = {'Content-Type': 'application/json'}
    if (sessionStorage.getItem('token')) {
        headers.token = sessionStorage.getItem('token')
    }   
    return await new Promise((resolve => {
        axios({
            headers: headers,
            method: method,
            url: baseUrl + url,
            data: (method === 'POST' || method === 'PUT') ? params : null,
            params: method === 'GET' ? params : null
        })
        .then((res) => {
            console.log(res.data)
            resolve(res.data)
        }).catch(e => {
             console.log(e)
        })
    }))
}

//get请求
const api = {   
    //get请求
    async get(url, params,) {
        return await apiAxios('GET', url, params)
    },
    //post请求
    async post (url, params) {
         return await apiAxios('POST', url, params)
    } ,
    //put请求
    async put(url, params) {
        return await apiAxios('PUT', url, params)
    },
    //delete请求
    async delete(url, params) {
        return await apiAxios('DELETE', url, params)
    }
}
export default api