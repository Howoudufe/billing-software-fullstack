import axios from "axios"; // axios 库，用来在前端发 HTTP 请求

export const addCategory = async (category) => {
    return await axios.post('http://localhost:8080/api/v1.0/categories', category);
}

export const deleteCategory = async (categoryID) => {
    return await axios.delete(`http://localhost:8080/api/v1.0/categories/${categoryID}`);
}

export const fetchCategories = async () => {
    return await axios.get('http://localhost:8080/api/v1.0/categories')
}