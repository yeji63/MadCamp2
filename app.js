const express = require('express')
const app = express()
const port = 80
const mongoClient = require('mongodb').MongoClient
const url = "mongodb://localhost:27017"
const http = require('http')
//const server = http.createServer((req, res) => {})

server=app.listen(port, () => {
    console.log(`Server is running at ${port}`);
})

io = require('socket.io')(server)

app.get('/', (req, res, next) => {
    res.send('hello world!')
})
app.use(express.json())
mongoClient.connect(url, (err, db) => {
    if(err) {
        console.log("Error while connecting mongo client")
    }        
    else {
        const myDb = db.db('myDb')
        const usercollection = myDb.collection('UserTable')
        const placecollection = myDb.collection('PlaceTable')
        const collection_chat = myDb.collection('collection_chat')
        io.on('connection', (socket) => {
            console.log("user connected!!")
        
            socket.on("join_chat", (nickname, roomname) => {
                socket.join(roomname)
                //console.log(id + " joined the chat " + roomname)
                io.sockets.in(roomname).emit('user_join_notif', nickname + " joined the chat")
                //io.to(roomname).emit('newUesrToChatRoom', id)
            })
        
            socket.on("left_chat", (nickname, roomname) => {
                socket.leave(roomname)
                //console.log(id + " left the chat " + roomname)
                io.sockets.in(roomname).emit('user_left_notif', nickname + " left the chat")
            })
        
            socket.on("new_message", (roomname, nickname, message) => {
                //console.log(id + " : " + message)
        
                let chatData = {
                    'roomname': roomname,
                    'nickname': nickname,
                    'message': message
                }
        
                console.log("new");
                console.log(chatData);
                io.sockets.in(roomname).emit('updatechat', chatData)
                //socket.broadcast.to(roomname).emit('updatechat', JSON.stringify(chatData))
            })
        
            socket.leave('room')
        })
        app.post('/signup', (req, res) => {
            const newUser = {
                nickname: req.body.nickname,
                profileimg: req.body.profileimg
            }
            const query = {nickname : newUser.nickname}
            usercollection.findOne(query, (err, result) => {
                if (result == null) {
                    usercollection.insertOne(newUser, (err, result) => {
                        res.status(200).send()
                    })
                } else {
                    res.status(400).send()
                }
            })
        })
        app.post('/addgroup', (req, res) => {
            const newGroup = {
                date: req.body.date,
                time: req.body.time,
                place: req.body.place,
                headcount: req.body.headcount,
                image: req.body.image,
                participants : [req.body.nickname],
                maker : req.body.nickname
            }           
            placecollection.insertOne(newGroup, (err, result) => {
            console.log(newGroup);
            res.status(200).send()
            })
        })
        app.post('/addaccount', (req, res) => {
            placecollection.findOne({$and:[{place: req.body.place}, {participants: req.body.nickname}, {time: req.body.time}, {maker: req.body.maker}]}, (err, result) => {
                console.log(result);
                if(result == null) {
                    console.log(result);
                    placecollection.update({$and:[{place: req.body.place}, {time: req.body.time}, {maker: req.body.maker}]}, {$push: {participants: req.body.nickname}}, (err, result) => {
                        res.status(200).send()
                    })
                }
                else {
                    console.log(result);
                    res.status(400).send()
                }
            })
        })
        app.get('/getgroup', (req, res) => {
            placecollection.find({}).toArray(function(err, result) {
                res.status(200).send(JSON.stringify(result))
            })
        })

        app.post('/deletegroup', (req, res) => {
            placecollection.deleteOne({$and:[{date : req.body.date},{time: req.body.time },{place: req.body.place}, {maker: req.body.maker}, {headcount: req.body.headcount}]}, (err, result) => {
                res.status(200).send()
            })
        })

        app.post('/getuserimg', (req, res) => {
            console.log(req.body.nickname);
            usercollection.find({nickname: {$in: req.body.nickname}}).toArray(function(err, result) {
                console.log(result);
                res.status(200).send(JSON.stringify(result))
            })
        })

        app.post('/deleteaccount', (req, res) => {
            console.log(req.body.nickname);
            placecollection.findOne({$and:[{place: req.body.place}, {participants: req.body.nickname}, {time: req.body.time}, {date: req.body.date}, {headcount: req.body.headcount}]}, (err, result) => {
                if(result == null) {
                    console.log(result);
                    res.status(400).send()
                }
                else {
                    console.log(result);
                    placecollection.update(result, {$pull: {participants: req.body.nickname}}, (err, result) => {
                        res.status(200).send()
                    })
                }
            })
        })

        //chatting
        app.post('/storechat', (req, res) => {

            console.log(req.body.nickname)
            console.log(req.body.message)
            const new_chat = {
                roomname: req.body.roomname,
                nickname: req.body.nickname,
                message: req.body.message
            }
        
            const find = {
                roomname: req.body.roomname
            }
        
            const change = {
                nickname: req.body.nickname[0],
                message: req.body.message[0]
            }
        
            //if doesn't exist, create, if exist, update
            const options = {
                upsert: true
            } 
        
            collection_chat.updateOne(find, {$push: change}, options, (err, result) => {
                console.log(change)
                res.status(200).send()
            })
        })
        
        app.post('/showchat', (req, res) => {
        
            const query = {
                roomname: req.body.roomname,
            }
        
            collection_chat.findOne(query, (err, result) => {
                if(result!=null){
                    res.status(200).send(JSON.stringify(result))
                } else {
                    res.status(400).send()
                }
                
            })
        })
    }
})


