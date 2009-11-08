package util;

public abstract class _semaphore {
    int _semaphore;
    int _busy = 0;
    int _free = 1;

    protected _semaphore() {
    	_semaphore = _free;
    }
    
    protected void _lock()
    {
        while(_semaphore != _free);
        _semaphore = _busy;
    }
   
    protected void _unlock()
    {
        _semaphore = _free;
    }
}
