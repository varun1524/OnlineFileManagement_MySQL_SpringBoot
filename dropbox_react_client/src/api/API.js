const api = process.env.REACT_APP_CONTACTS_API_URL || 'http://localhost:8080';

const headers = {
    'Accept': 'application/json'
};

export const doSignUp = (payload) =>
    fetch (`${api}/users/doSignUp`,
        {
            method: 'POST',
            headers: {
                ...headers,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        }).then(res => {
        return res.status;
    }).catch(error => {
        console.log("Error: "+error);
        return error;
    });

export const doLogin = (payload) =>
    fetch(`${api}/users/doLogin`, {
        method: 'POST',
        headers: {
            ...headers,
            'Content-Type': 'application/json'
        },
        credentials:'include',
        body: JSON.stringify(payload)
    }).then(res => {
        return res.status;
    })
        .catch(error => {
            console.log("This is error");
            return error;
        });

export const getSession = () =>
    fetch(`${api}/users/getSession`, {
        method: 'POST',
        headers: {
            ...headers,
            'Content-Type': 'application/json'
        },
        credentials:'include'
    }).then(res => {
        return res;
    })
        .catch(error => {
            console.log("This is error");
            return error;
        });

export const doLogout = (payload) =>
    fetch(`${api}/users/doLogout`, {
        method: 'POST',
        headers: {
            ...headers,
            'Content-Type': 'application/json'
        },
        credentials:'include',
        body: JSON.stringify(payload)
    }).then(res => {
        return res.status;
    })
        .catch(error => {
            console.log("This is error");
            return error;
        });

export const getDirectoryData = (payload) =>
    fetch(`${api}/file/getDirData`,{
        method:'POST',
        headers: {
            ...headers,
            'Content-Type': 'application/json'
        },
        credentials:'include',
        body: JSON.stringify(payload)
    }).then(res => {return res;})
        .catch(error => {
            console.log("This is error.");
            return error;
        });

export const uploadFile = (payload) =>
    fetch(`${api}/file/upload`, {
            method: 'POST',
            credentials:'include',
            body: payload
        }
    ).then(res => {
        console.log("My response in upload file is ",JSON.stringify(res));
        return res;
    }).catch(error => {
        console.log(payload);
        console.log("This is error while file upload ", error.message);
        return error;
    });

export const createDirectory = (payload) =>
    fetch(`${api}/file/createDir`, {
        method: 'POST',
        headers: {
            ...headers,
            'Content-Type': 'application/json'
        },
        body:JSON.stringify(payload),
        credentials:'include'
    }).then(res => {
        return res;
    })
        .catch(error => {
            console.log("This is error");
            return error;
        });


export const doShareData = (payload) =>
    fetch (`${api}/share/share`,
        {
            method: 'POST',
            headers: {
                ...headers,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload),
            credentials: 'include'
        }).then(res => {
        return res;
    }).catch(error => {
        console.log("Error: " + error);
        return error;
    });

export const doRemoveSharing = (payload) =>
    fetch (`${api}/users/removesharing`,
        {
            method: 'POST',
            headers: {
                ...headers,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload),
            credentials: 'include'
        }).then(res => {
        return res;
    }).catch(error => {
        console.log("Error: " + error);
        return error;
    });

export const changeStarredStatus = (payload) =>
    fetch (`${api}/file/changestarredstatus`,
        {
            method: 'POST',
            headers: {
                ...headers,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload),
            credentials: 'include'
        }).then(res => {
        return res;
    }).catch(error => {
        console.log("Error: " + error);
        return error;
    });

export const fetchStarredData = (payload) =>
    fetch (`${api}/file/getStarredData`,
        {
            method: 'POST',
            headers: {
                ...headers,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload),
            credentials: 'include'
        }).then(res => {
        return res;
    }).catch(error => {
        console.log("Error: " + error);
        return error;
    });

export const fetchDataSharedByUser = (payload) =>
    fetch (`${api}/share/getDataSharedByUser`,
        {
            method: 'POST',
            headers: {
                ...headers,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload),
            credentials: 'include'
        }).then(res => {
        return res;
    }).catch(error => {
        console.log("Error: " + error);
        return error;
    });


export const fetchDataSharedWithUser = (payload) =>
    fetch (`${api}/share/fetchDataSharedWithUser`,
        {
            method: 'POST',
            headers: {
                ...headers,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload),
            credentials: 'include'
        }).then(res => {
        return res;
    }).catch(error => {
        console.log("Error: " + error);
        return error;
    });

export const accessSharedData = (payload) =>
    fetch (`${api}/share/accessSharedData`,
        {
            method: 'POST',
            headers: {
                ...headers,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload),
            credentials: 'include'
        }).then(res => {
        return res;
    }).catch(error => {
        console.log("Error: " + error);
        return error;
    });

export const changeProfile = (payload) =>
    fetch (`${api}/profile/changeProfile`,
        {
            method: 'POST',
            headers: {
                ...headers,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload),
            credentials: 'include'
        }).then(res => {
        return res;
    }).catch(error => {
        console.log("Error: " + error);
        return error;
    });

export const getprofile = () =>
    fetch (`${api}/profile/getprofile`, {
        method: 'POST',
        headers: {
            ...headers,
            'Content-Type': 'application/json'
        },
        credentials:'include'
    }).then(res => {
        return res;
    }).catch(error => {
            console.log("This is error");
            return error;
    });

export const getUserActivityData = () =>
    fetch (`${api}/activity/getUserActivityData`,
        {
            method: 'POST',
            headers: {
                ...headers,
                'Content-Type': 'application/json'
            },
            credentials: 'include'
        }).then(res => {
        return res;
    }).catch(error => {
        console.log("Error: " + error);
        return error;
    });

export const getStorageActivityData = () =>
    fetch (`${api}/activity/getStorageActivityData`,
        {
            method: 'POST',
            headers: {
                ...headers,
                'Content-Type': 'application/json'
            },
            credentials: 'include'
        }).then(res => {
        return res;
    }).catch(error => {
        console.log("Error: " + error);
        return error;
    });

export const createGroup = (payload) =>
    fetch(`${api}/group/creategroup`, {
        method: 'POST',
        headers: {
            ...headers,
            'Content-Type': 'application/json'
        },
        credentials:'include',
        body : JSON.stringify(payload),
    }).then(res => {
        return res;
    })
        .catch(error => {
            console.log("This is error");
            return error;
        });

export const deleteContent = (payload) =>
    fetch (`${api}/file/deleteContent`,
        {
            method: 'POST',
            headers: {
                ...headers,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload),
            credentials: 'include'
        }).then(res => {
        return res;
    }).catch(error => {
        console.log("Error: " + error);
        return error;
    });

export const deleteGroup = (payload) =>
    fetch (`${api}/group/deletegroup`,
        {
            method: 'POST',
            headers: {
                ...headers,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload),
            credentials: 'include'
        }).then(res => {
        return res;
    }).catch(error => {
        console.log("Error: " + error);
        return error;
    });

export const deleteMember = (payload) =>
    fetch (`${api}/group/deletemember`,
        {
            method: 'POST',
            headers: {
                ...headers,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload),
            credentials: 'include'
        }).then(res => {
        return res;
    }).catch(error => {
        console.log("Error: " + error);
        return error;
    });

export const deletecontentfromgroup = (payload) =>
    fetch (`${api}/group/deletecontent`,
        {
            method: 'POST',
            headers: {
                ...headers,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload),
            credentials: 'include'
        }).then(res => {
        return res;
    }).catch(error => {
        console.log("Error: " + error);
        return error;
    });



export const getGroups = () =>
    fetch (`${api}/group/getgroups`,
        {
            method: 'POST',
            headers: {
                ...headers,
                'Content-Type': 'application/json'
            },
            credentials: 'include'
        }).then(res => {
        return res;
    }).catch(error => {
        console.log("Error: " + error);
        return error;
    });

export const getGroupData = (payload) =>
    fetch(`${api}/group/getgroupdata`,{
        method:'POST',
        headers: {
            ...headers,
            'Content-Type': 'application/json'
        },
        credentials:'include',
        body: JSON.stringify(payload)
    }).then(res => {return res;})
        .catch(error => {
            console.log("This is error.");
            return error;
        });

export const createDirectoryInGroup = (payload) =>
    fetch(`${api}/group/createDir`, {
        method: 'POST',
        headers: {
            ...headers,
            'Content-Type': 'application/json'
        },
        body:JSON.stringify(payload),
        credentials:'include'
    }).then(res => {
        return res;
    })
        .catch(error => {
            console.log("This is error");
            return error;
        });

export const fetchGroupAccessDetails = (payload) =>
    fetch(`${api}/group/fetchgroupaccessdetails`,{
        method:'POST',
        headers: {
            ...headers,
            'Content-Type': 'application/json'
        },
        credentials:'include',
        body: JSON.stringify(payload)
    }).then(res => {return res;})
        .catch(error => {
            console.log("This is error.");
            return error;
        });


export const getGroupMembers = (payload) =>
    fetch(`${api}/group/getgroupmembers`,{
        method:'POST',
        headers: {
            ...headers,
            'Content-Type': 'application/json'
        },
        credentials:'include',
        body: JSON.stringify(payload)
    }).then(res => {return res;})
        .catch(error => {
            console.log("This is error.");
            return error;
        });

export const uploadFileInGroup = (payload) =>
    fetch(`${api}/group/upload`, {
        method: 'POST',
        headers: {
            ...headers,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload),
        credentials:'include'
    }).then(res => {
        return res;
    }).catch(error => {
        console.log("This is error");
        return error;
    });

export const addMembersInGroup = (payload) =>
    fetch(`${api}/group/addmember`, {
        method: 'POST',
        headers: {
            ...headers,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload),
        credentials:'include'
    }).then(res => {
        return res;
    }).catch(error => {
        console.log("This is error");
        return error;
    });

// export const fetchSelectedDataSharedWithUser = (payload) =>
//     fetch (`${api}/users/accessSelectedSharedData`,
//         {
//             method: 'POST',
//             headers: {
//                 ...headers,
//                 'Content-Type': 'application/json'
//             },
//             body: JSON.stringify(payload),
//             credentials: 'include'
//         }).then(res => {
//         return res;
//     }).catch(error => {
//         console.log("Error: " + error);
//         return error;
//     });



// export const getStarredDirectoryData =

/*
export const doCalculate = (payload) =>
    fetch (`${api}/users/doCalculate`,
        {
            method: 'POST',
            headers: {
                ...headers,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        }).then(res => {
        return res.json();
    }).catch(error => {
        console.log("Error: "+error);
        return error;
    });*/
