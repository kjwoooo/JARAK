import {useEffect, useState} from "react";
import axios from "axios";
import './App.css';

function App() {

    let [hello, setHello] = useState('');

    useEffect(() => {
            axios.get('/api/test')
                .then((res) => {
                    setHello(res.data);
                })
        }, []);

  return (
    <div className="App">
        백엔드 데이터 : {hello}
    </div>
  );
}

export default App;
